package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class SprintingCondition extends AbilityCondition
{
	public SprintingCondition()
	{
		super(ability -> ability.getEntity().isSprinting(), new TextComponentTranslation("condition.sprinting"));
	}
}
