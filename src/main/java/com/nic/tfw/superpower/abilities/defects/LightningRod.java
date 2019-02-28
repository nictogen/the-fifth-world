package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class LightningRod extends AbilityConstant
{
	public LightningRod(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && this.ticks % 20 == 0){
			this.entity.world.addWeatherEffect(new EntityLightningBolt(this.entity.world, entity.posX, entity.posY, entity.posZ, (!(entity instanceof EntityPlayer) && !entity.world.getGameRules().getBoolean("mobGriefing")) || (entity instanceof EntityPlayer && MinecraftForge.EVENT_BUS.post(new BlockEvent.PlaceEvent(new BlockSnapshot(entity.world, entity.getPosition(), Blocks.FIRE.getDefaultState()), entity.world.getBlockState(entity.getPosition()), (EntityPlayer) entity, EnumHand.MAIN_HAND)))));
		}

	}
}
