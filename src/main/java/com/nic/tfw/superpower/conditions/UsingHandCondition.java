package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class UsingHandCondition extends AbilityCondition
{
	public UsingHandCondition()
	{
		super(ability -> ability.getEntity().isHandActive(), new TextComponentTranslation("condition.using_hand"));
	}
}
