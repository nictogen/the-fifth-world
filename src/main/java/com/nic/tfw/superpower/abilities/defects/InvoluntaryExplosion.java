package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/16/19.
 */
public class InvoluntaryExplosion extends AbilityConstant
{
	public InvoluntaryExplosion(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote)
		entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 5.0f, true);
	}

}
