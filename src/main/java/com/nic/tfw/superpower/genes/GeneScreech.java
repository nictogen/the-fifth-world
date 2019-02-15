package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityScreech;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneScreech extends Gene
{
	private float maxDamage;

	public GeneScreech(String displayName, float damage)
	{
		super(AbilityScreech.class, displayName);
		this.maxDamage = damage;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return (((AbilityScreech)ability).damage / maxDamage) + r.nextFloat() * 0.2f;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityScreech(entity, geneData.quality * this.maxDamage);
	}
}
