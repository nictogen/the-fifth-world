package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class Starving extends AbilityConstant
{
	public Starving(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void updateTick()
	{
		if(this.entity instanceof EntityPlayer && this.ticks % 20 == 0)
				((EntityPlayer) this.entity).getFoodStats().addExhaustion(1.0f);
	}
}
