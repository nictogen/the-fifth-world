package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityMountable extends AbilityConstant
{
	public AbilityMountable(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{

	}

	@Mod.EventBusSubscriber
	public static class Handler {

		@SubscribeEvent
		public static void onRightClick(PlayerInteractEvent.EntityInteract event){
			if(event.getTarget() instanceof EntityLivingBase && event.getTarget().getPassengers().isEmpty())
			{
				for (Ability currentAbility : Ability.getAbilities((EntityLivingBase) event.getTarget()))
				{
					if(currentAbility instanceof AbilityMountable && currentAbility.isUnlocked()){
						event.getEntityLiving().startRiding(event.getTarget());
					}
				}
			}
		}
	}
}
