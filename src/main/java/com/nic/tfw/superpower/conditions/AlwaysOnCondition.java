package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class AlwaysOnCondition extends AbilityCondition
{
	public AlwaysOnCondition()
	{
		super(ability -> true, new TextComponentTranslation("condition.always_on"));
	}
}
