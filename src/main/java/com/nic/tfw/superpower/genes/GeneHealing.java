package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityHealing;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/24/19.
 */
public class GeneHealing extends Gene
{
	private float maxAmount;

	public GeneHealing(String displayName, float maxAmount)
	{
		super(AbilityHealing.class, displayName);
		this.maxAmount = maxAmount;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return ((AbilityHealing) ability).getAmount() / maxAmount;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, int.class, float.class).newInstance(entity, 20,  maxAmount * geneData.quality);
	}
}
