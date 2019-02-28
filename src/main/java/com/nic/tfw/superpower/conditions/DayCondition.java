package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class DayCondition extends AbilityCondition
{
	public DayCondition()
	{
		super(ability -> ability.getEntity().world.isDaytime(), new TextComponentTranslation("condition.daytime"));
	}
}

