package com.nic.tfw.superpower.abilities.defects;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class Deafness extends AbilityConstant
{
	public Deafness(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void updateTick()
	{
		if(entity.world.isRemote && entity instanceof EntityPlayer)
			TheFifthWorld.proxy.stopSounds((EntityPlayer) entity);
	}
}
