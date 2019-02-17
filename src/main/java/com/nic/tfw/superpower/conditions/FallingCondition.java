package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class FallingCondition extends AbilityCondition
{
	public FallingCondition()
	{
		super(ability -> ability.getEntity().fallDistance > 0, new TextComponentTranslation("condition.falling"));
	}
}
