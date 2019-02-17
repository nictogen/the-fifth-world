package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;

/**
 * Created by Nictogen on 1/16/19.
 */
public interface IDefect
{
	void setCondition(AbilityCondition condition);

	AbilityCondition getCondition();
}
