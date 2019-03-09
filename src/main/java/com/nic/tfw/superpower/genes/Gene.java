package com.nic.tfw.superpower.genes;

import com.nic.tfw.util.ListUtil;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEntry;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataManager;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityConditionOr;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Nictogen on 1/12/19.
 */

public class Gene extends IForgeRegistryEntry.Impl<Gene>
{
	public AbilityEntry ability;
	public String displayName;

	private ArrayList<DataMod> dataMods = new ArrayList<>();

	public Gene(Class<? extends Ability> c, String displayName, boolean dontRegister)
	{
		this.ability = ListUtil.firstMatches(Ability.ABILITY_REGISTRY.getValuesCollection(), abilityEntry1 -> abilityEntry1.getAbilityClass() == c);
		this.displayName = displayName;
		if (!dontRegister)
			setRegistryName(ability.getRegistryName());
	}

	public Gene(Class<? extends Ability> c, String displayName)
	{
		this(c, displayName, false);
	}

	public boolean isQualified(){
		for (DataMod dataMod : dataMods)
			if(dataMod.isQualified) return true;
		return false;
	}
	float getQuality(Ability ability, Random r)
	{
		float quality = 0f;
		ArrayList<DataMod> list = new ArrayList<>(dataMods);
		list.removeIf(dataMod -> !dataMod.isQualified && ability.getDataManager().has(dataMod.data));
		for (DataMod dataMod : list)
		{
			@SuppressWarnings("unchecked") float abilityVal = (float) ability.getDataManager().get(dataMod.data);
			float maxVal = (float) dataMod.value;

			if (!dataMod.isReversed)
				quality += abilityVal / maxVal;
			else
				quality += 1f - (abilityVal / maxVal);
		}
		quality /= (float) list.size();
		return quality;
	}

	public void serializeExtra(GeneSet.GeneData geneData, NBTTagCompound compound)
	{
	}

	public Gene addDataMod(DataMod mod){
		this.dataMods.add(mod);
		return this;
	}

	public Gene addCooldown(int fairCooldown){
		this.dataMods.add(new Gene.DataMod<>(Ability.MAX_COOLDOWN, (int) ((float) fairCooldown / 0.15f), true, true));
		return this;
	}

	public Ability getAbility(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		try
		{
			Ability a = ability.getAbilityClass().getConstructor(EntityLivingBase.class).newInstance(entity);
			ArrayList<AbilityCondition> list = new ArrayList<>();
			for (AbilityCondition.ConditionEntry condition : geneData.conditions)
				list.add(condition.getConditionClass().newInstance());
			if(!list.isEmpty())
				a.addCondition(new AbilityConditionOr(list.toArray(new AbilityCondition[0])));
			AbilityDataManager abilityDataManager = a.getDataManager();
			abilityDataManager.set(Ability.TITLE, new TextComponentTranslation(geneData.gene.displayName));
			for (DataMod dataMod : dataMods)
			{
				if (abilityDataManager.has(dataMod.data))
				{
					//noinspection unchecked
					abilityDataManager.set(dataMod.data, dataMod.getValue(geneData));
				}
			}
			return a;
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public GeneSet.GeneData combine(GeneSet.GeneData one, GeneSet.GeneData two)
	{
		Set<UUID> donors = new HashSet<>(one.donors);
		donors.addAll(two.donors);

		HashSet<AbilityCondition.ConditionEntry> conditions = new HashSet<>(one.conditions);
		conditions.addAll(two.conditions);

		return new GeneSet.GeneData(one.gene, donors, one.gene instanceof GeneDefect ? one.quality : one.quality + two.quality, conditions);
	}

	public static class DataMod<T>
	{
		private T value;
		private boolean isQualified, isReversed;
		public AbilityData<T> data;

		public DataMod(AbilityData<T> data, T value, boolean isQualified, boolean isReversed)
		{
			this.data = data;
			this.value = value;
			this.isQualified = isQualified;
			this.isReversed = isReversed;
		}

		public DataMod(AbilityData<T> data, T value, boolean isQualified)
		{
			this(data, value, isQualified, false);
		}

		public DataMod(AbilityData<T> data, T value)
		{
			this(data, value, true, false);
		}

		@SuppressWarnings("unchecked") T getValue(GeneSet.GeneData geneData)
		{
			if (isQualified)
			{
				if(value instanceof Integer){
					Integer i = (!isReversed) ? new Integer((int) (((Integer) value).floatValue() * geneData.quality)) : new Integer((int) (((Integer) value).floatValue() *  (1f - geneData.quality/3.5f)));
					if(i < 1)
						i = 1;
					return (T) i;
				} else if(value instanceof Double){
					return (!isReversed) ? (T) new Double((int) (((Double) value).floatValue() * geneData.quality)) : (T) new Double((int) (((Double) value).floatValue() *  (1f - geneData.quality/3.5f)));
				} else if(value instanceof Float){
					return (!isReversed) ? (T) new Float((int) ((Float) value * geneData.quality)) : (T) new Float((int) ((Float) value *  (1f - geneData.quality/3.5f)));
				}
			}
			return value;
		}
	}
}

