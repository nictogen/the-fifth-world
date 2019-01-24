package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityPotionPunch;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/21/19.
 */
public class GenePotionPunch extends Gene
{
	private float maxDuration;
	private float maxAmplifier;
	private Potion potion;

	public GenePotionPunch(String displayName, Potion potion, int maxDuration, int maxAmplifier)
	{
		super(AbilityPotionPunch.class, displayName, true);
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
		this.maxDuration = maxDuration;
		this.maxAmplifier = maxAmplifier;
		this.potion = potion;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		AbilityPotionPunch a = (AbilityPotionPunch) ability;
		Field duration = AbilityPotionPunch.class.getDeclaredFields()[2];
		duration.setAccessible(true);
		Field amplifier = AbilityPotionPunch.class.getDeclaredFields()[1];
		amplifier.setAccessible(true);
		try
		{
			int d = (int) duration.get(a);
			int amp = (int) amplifier.get(a);
			return ((((float)d)/ maxDuration) + (((float)amp)/maxAmplifier))/2f;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, Potion.class, int.class, int.class, int.class).newInstance(entity, potion, Math.max(1, (int) (geneData.quality * maxAmplifier)),  Math.max(1, (int) (geneData.quality * maxDuration)), 0);
	}
}
