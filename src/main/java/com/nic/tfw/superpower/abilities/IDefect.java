package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;

/**
 * Created by Nictogen on 1/16/19.
 */
public interface IDefect
{
	void setCondition(Condition condition);

	Condition getCondition();
}
