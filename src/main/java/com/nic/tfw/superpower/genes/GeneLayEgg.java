package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityLayEgg;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneLayEgg extends Gene
{
	private int maxCooldown;

	public GeneLayEgg(String displayName, int maxCooldown)
	{
		super(AbilityLayEgg.class, displayName);
		this.maxCooldown = maxCooldown;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 1f - ((float) ability.getCooldown() / (float) maxCooldown);
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityLayEgg(entity, (int) ((1f - geneData.quality)*maxCooldown));
	}
}
