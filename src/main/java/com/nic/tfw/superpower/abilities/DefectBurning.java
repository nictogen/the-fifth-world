package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectBurning extends AbilityConstant implements IDefect
{
	private AbilityCondition condition;

	public DefectBurning(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && getCondition().test(this))
			entity.setFire(1);
	}

	@Override public void setCondition(AbilityCondition condition)
	{
		this.condition = condition;
	}

	@Override public AbilityCondition getCondition()
	{
		return this.condition;
	}
}
