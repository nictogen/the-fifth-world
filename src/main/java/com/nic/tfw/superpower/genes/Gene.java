package com.nic.tfw.superpower.genes;

import com.nic.tfw.util.ListUtil;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nictogen on 1/12/19.
 */

public abstract class Gene extends IForgeRegistryEntry.Impl<Gene>
{
	public Ability.AbilityEntry ability;
	public String displayName;

	public Gene(Class<? extends Ability> c, String displayName)
	{
		this.ability = ListUtil.firstMatches(Ability.ABILITY_REGISTRY.getValuesCollection(), abilityEntry1 -> abilityEntry1.getAbilityClass() == c);
		setRegistryName(ability.getRegistryName());
		this.displayName = displayName;
	}

	public Gene(Class<? extends Ability> c, String displayName, boolean dontRegister)
	{
		this.ability = ListUtil.firstMatches(Ability.ABILITY_REGISTRY.getValuesCollection(), abilityEntry1 -> abilityEntry1.getAbilityClass() == c);
		this.displayName = displayName;
	}

	abstract float getQuality(Ability ability, Random r);

	public void serializeExtra(GeneSet.GeneData geneData, NBTTagCompound compound){}

	public Ability getAbility(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		try
		{
			Ability ab = createAbilityInstance(entity, geneData);
			return ab;
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public abstract Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

	public GeneSet.GeneData combine(GeneSet.GeneData one, GeneSet.GeneData two){
		ArrayList<UUID> donors = new ArrayList<>();

		for (UUID donor : one.donors)
			for (UUID uuid : two.donors)
				if(uuid.equals(donor)) return one;

		donors.addAll(one.donors);
		donors.addAll(two.donors);

		return new GeneSet.GeneData(one.gene, donors, one.quality + two.quality);
	}


}

