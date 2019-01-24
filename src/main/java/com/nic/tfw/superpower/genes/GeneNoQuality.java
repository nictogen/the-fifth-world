package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/24/19.
 */
public class GeneNoQuality extends Gene
{
	public GeneNoQuality(Class<? extends Ability> c, String displayName)
	{
		super(c, displayName);
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 1.0f;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class).newInstance(entity);
	}
}
