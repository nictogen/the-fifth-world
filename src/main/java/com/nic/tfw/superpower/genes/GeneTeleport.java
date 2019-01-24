package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityTeleport;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/24/19.
 */
public class GeneTeleport extends Gene
{
	private float maxDistance;

	public GeneTeleport(String displayName, float maxDistance)
	{
		super(AbilityTeleport.class, displayName);
		this.maxDistance = maxDistance;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		AbilityTeleport a = (AbilityTeleport) ability;
		Field f = AbilityTeleport.class.getDeclaredFields()[0];
		f.setAccessible(true);
		try
		{
			float distance = (float) f.get(a);
			return distance / maxDistance;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, float.class, int.class).newInstance(entity, geneData.quality * maxDistance,  0);
	}
}
