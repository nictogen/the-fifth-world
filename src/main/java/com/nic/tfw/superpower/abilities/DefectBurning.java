package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectBurning extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectBurning(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && getCondition().isEnabled(entity, this))
			entity.setFire(1);
	}

	@Override public void setCondition(Condition condition)
	{
		this.condition = condition;
	}

	@Override public Condition getCondition()
	{
		return this.condition;
	}
}
