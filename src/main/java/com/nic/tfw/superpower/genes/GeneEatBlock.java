package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityEatBlock;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneEatBlock extends Gene
{
	private IBlockState to;
	private IBlockState from;
	private float maxSat;
	private int maxFood;

	public GeneEatBlock(String displayName, IBlockState from, IBlockState to, int maxFood, float maxSat)
	{
		super(AbilityEatBlock.class, displayName, true);
		this.to = to;
		this.from = from;
		this.maxFood = maxFood;
		this.maxSat = maxSat;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return ((((float) ((AbilityEatBlock)ability).getHungerIncrease() / (float) maxFood) + ((AbilityEatBlock)ability).getSaturationIncrease() / maxSat) / 2) + r.nextFloat() * 0.2f;
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityEatBlock(entity, from, to, (int) (maxFood * geneData.quality), maxSat * geneData.quality);
	}
}
