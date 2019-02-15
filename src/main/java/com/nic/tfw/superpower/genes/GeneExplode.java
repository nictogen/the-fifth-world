package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityExplode;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneExplode extends Gene
{
	private float maxStrength;
	private int maxCooldown;

	public GeneExplode(String displayName, float maxStrength, int maxCooldown)
	{
		super(AbilityExplode.class, displayName);
		this.maxStrength = maxStrength;
		this.maxCooldown = maxCooldown;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return (((AbilityExplode)ability).strength / maxStrength + 1f - ((float) ability.getCooldown() / (float) maxCooldown)) / 2f;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityExplode(entity, maxStrength * geneData.quality, (int) ((1f - geneData.quality)*maxCooldown));
	}
}
