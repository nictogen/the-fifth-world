package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectButterFingers extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectButterFingers(EntityPlayer player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!player.world.isRemote && getCondition().isEnabled(player, this))
			player.dropItem(false);
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

