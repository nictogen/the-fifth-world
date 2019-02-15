package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityEatBlock;
import net.minecraft.block.Block;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneEatBlock extends Gene
{
	public GeneEatBlock(String displayName, Block from, Block to, int maxFood, float maxSat)
	{
		super(AbilityEatBlock.class, displayName, true);
		addDataMod(new DataMod<>(AbilityEatBlock.TO, to));
		addDataMod(new DataMod<>(AbilityEatBlock.FROM, from));
		addDataMod(new DataMod<>(AbilityEatBlock.FOOD, maxFood));
		addDataMod(new DataMod<>(AbilityEatBlock.SATURATION, maxSat));
	}
}
