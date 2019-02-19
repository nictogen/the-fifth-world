package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;

import java.util.Random;

/**
 * Created by Nictogen on 1/18/19.
 */
public class GeneDefect extends Gene
{
	private float alwaysOnChance = 0.0f;

	public GeneDefect(Class<? extends Ability> c, String displayName)
	{
		super(c, displayName);
	}

	GeneDefect setAlwaysOnChance(float chance)
	{
		alwaysOnChance = chance;
		return this;
	}

	float getAlwaysOnChance()
	{
		return alwaysOnChance;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 0;
	}
}
