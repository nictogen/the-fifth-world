package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class SwimmingCondition extends AbilityCondition
{
	public SwimmingCondition()
	{
		super(ability -> ability.getEntity().isInWater(), new TextComponentTranslation("condition.swimming"));
	}
}
