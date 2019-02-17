package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectButterFingers extends AbilityConstant implements IDefect
{
	private AbilityCondition condition;

	public DefectButterFingers(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && getCondition().test(this))
		{
			//TODO others
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).dropItem(false);
		}
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

