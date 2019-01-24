package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityFirePunch;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/24/19.
 */
public class GeneFirePunch extends Gene
{
	private float maxDuration;
	public GeneFirePunch(String displayName, float maxDuration)
	{
		super(AbilityFirePunch.class, displayName);
		this.maxDuration = maxDuration;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		AbilityFirePunch a = (AbilityFirePunch) ability;
		Field f = AbilityFirePunch.class.getDeclaredFields()[0];
		f.setAccessible(true);
		try
		{
			float duration = (int) f.get(a);
			return duration / maxDuration;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, int.class, int.class).newInstance(entity, (int) Math.max(1, (int) (geneData.quality * maxDuration)),  0);
	}
}
