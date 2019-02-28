package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class NightCondition extends AbilityCondition
{
	public NightCondition()
	{
		super(ability -> !ability.getEntity().world.isDaytime(), new TextComponentTranslation("condition.nighttime"));
	}
}

