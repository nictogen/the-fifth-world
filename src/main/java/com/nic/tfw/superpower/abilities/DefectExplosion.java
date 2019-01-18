package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Nictogen on 1/16/19.
 */
public class DefectExplosion extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectExplosion(EntityPlayer player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!player.world.isRemote && getCondition().isEnabled(player, this))
			player.world.createExplosion(null, player.posX, player.posY, player.posZ, 5.0f, true);
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
