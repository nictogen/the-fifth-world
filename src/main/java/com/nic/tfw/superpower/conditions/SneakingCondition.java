package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class SneakingCondition extends AbilityCondition
{
	public SneakingCondition()
	{
		super(ability -> ability.getEntity().isSneaking(), new TextComponentTranslation("condition.sneaking"));
	}
}
