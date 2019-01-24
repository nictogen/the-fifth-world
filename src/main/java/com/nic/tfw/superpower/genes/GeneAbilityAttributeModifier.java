package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAttributeModifier;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilitySprint;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nictogen on 1/18/19.
 */
public class GeneAbilityAttributeModifier extends Gene
{

	private float perfectValue;
	public GeneAbilityAttributeModifier(Class<? extends AbilityAttributeModifier> c, String displayName, float perfectValue)
	{
		super(c, displayName);
		this.perfectValue = perfectValue;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		AbilityAttributeModifier a = (AbilityAttributeModifier) ability;

		return (a.getFactor() / perfectValue) + ((float) r.nextGaussian() * 0.25f);
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		if(ability.getAbilityClass() == AbilitySprint.class)
			return ability.getAbilityClass().getConstructor(EntityLivingBase.class, UUID.class, float.class).newInstance(entity, GeneHandler.RANDOM_UUID, perfectValue*geneData.quality);
		else return ability.getAbilityClass().getConstructor(EntityLivingBase.class, UUID.class, float.class, int.class).newInstance(entity, GeneHandler.RANDOM_UUID, perfectValue*geneData.quality, 0);
	}
}
