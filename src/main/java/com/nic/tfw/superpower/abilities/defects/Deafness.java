package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(value = Side.CLIENT)
	public static class Handler {

		@SubscribeEvent
		public static void onSound(PlaySoundEvent event){
			EntityPlayer player = Minecraft.getMinecraft().player;
			if(player == null) return;
			for (Deafness abilitiesFromClass : Ability.getAbilitiesFromClass(Ability.getAbilities(player), Deafness.class))
			{
				if(abilitiesFromClass.isUnlocked())
					event.setResultSound(null);
			}
		}
	}
}
