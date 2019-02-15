package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityPotionImmunity;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GenePotionImmunity extends Gene
{
	private Potion potion;

	public GenePotionImmunity(String displayName, Potion potion)
	{
		super(AbilityPotionImmunity.class, displayName, false);
		this.potion = potion;
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 1.0f;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityPotionImmunity(entity, potion);
	}
}
