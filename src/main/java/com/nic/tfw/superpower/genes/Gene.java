package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Nictogen on 1/12/19.
 */

public class Gene extends IForgeRegistryEntry.Impl<Gene>
{
	public Ability.AbilityEntry ability;
	public int[] fields;
	public Object[] maxValues;
	public String displayName;


	public Gene(){}
	public Gene(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName)
	{
		Optional<Ability.AbilityEntry> abilityEntry = Ability.ABILITY_REGISTRY.getValuesCollection().stream().filter(
				abilityEntry1 -> abilityEntry1.getAbilityClass() == c).findFirst();
		this.ability = abilityEntry.get();
		this.fields = fields;
		this.maxValues = maxValues;
		this.displayName = displayName;
		setRegistryName(ability.getRegistryName());
	}

	public NBTTagCompound createAbilityTagFromAbility(Random r, Ability ability) throws IllegalAccessException
	{
		return createAbilityTag(getQualityFromValues(ability, r));
	}

	public NBTTagCompound createAbilityTag(Random r, GeneHandler.GeneStrength strength)
	{
		return createAbilityTag(r.nextFloat() * strength.chance);
	}

	public NBTTagCompound createAbilityTag(float quality)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString(GeneHandler.GENE_REGISTRY_NAME_TAG, ability.getRegistryName().toString());
		compound.setFloat(GeneHandler.GENE_QUALITY_TAG, quality);
		return compound;
	}

	private float alwaysOnChance = 0.0f;

	public Gene setAlwaysOnChance(float chance)
	{
		alwaysOnChance = chance;
		return this;
	}

	public float getAlwaysOnChance()
	{
		return alwaysOnChance;
	}

	public Ability getAbility(EntityLivingBase entity, float quality, NBTTagCompound extraData)
	{
		try
		{
			Ability ab = createAbilityInstance(entity, extraData);
			for (int i = 0; i < fields.length; i++)
				setFieldValue(ab, i, quality);
			return ab;
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Ability createAbilityInstance(EntityLivingBase entity, NBTTagCompound nbt) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class).newInstance(entity);
	}

	public Field getField(int fieldIndex){
		int n = fields[fieldIndex];
		Class c = ability.getAbilityClass();
		if (n < 0)
		{
			while (n <= -100)
			{
				n += 100;
				c = c.getSuperclass();
			}
			n *= -1;
		}
		Field f = c.getDeclaredFields()[n];
		f.setAccessible(true);
		return f;
	}

	public void setFieldValue(Ability ab, int fieldIndex, float quality) throws IllegalAccessException
	{
		Field f = getField(fieldIndex);
		Object o = maxValues[fieldIndex];

		if (o instanceof Integer || o instanceof Double || o instanceof Float || o instanceof Long)
		{
			if (o instanceof Integer)
				f.set(ab, Math.max((int) ((((Integer) o).floatValue()) * quality), 1));
			 else if (o instanceof Double)
				f.set(ab, (double) ((((Double) o).floatValue()) * quality));
			else if (o instanceof Long)
				f.set(ab, (long) ((((Long) o).floatValue()) * quality));
			else
				f.set(ab, (Float) o * quality);
		}
		else
			f.set(ab, maxValues[fieldIndex]);
	}

	private float getQualityFromValues(Ability ab, Random r) throws IllegalAccessException
	{
		if (fields.length == 0 || maxValues.length == 0)
		{
			return 1.0f;
		}
		else
		{

			float quality = 0.0f;
			for (int i = 0; i < fields.length; i++)
			{
				Field f = getField(i);
				Object o = f.get(ab);
				if (o instanceof Integer)
				{
					quality += (int) o;
				}
				else if (o instanceof Double)
				{
					quality += (double) o;
				}
				else if (o instanceof Long)
				{
					quality += (double) o;
				}
				else if (o instanceof Float)
				{
					quality += (float) o;
				}
			}

			quality = quality / ((float) fields.length);

			return quality + ((float) r.nextGaussian() * 0.25f);
		}
	}

	public void combineGenes(NBTTagCompound one, NBTTagCompound two){

	}
}

