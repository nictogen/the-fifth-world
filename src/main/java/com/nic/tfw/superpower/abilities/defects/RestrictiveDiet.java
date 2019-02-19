package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 1/17/19.
 */
public class RestrictiveDiet extends AbilityConstant
{
	public RestrictiveDiet(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{

	}

	//TODO some available foods?
	@Mod.EventBusSubscriber
	public static class Handler {
		@SubscribeEvent
		public static void onEat(LivingEntityUseItemEvent event){
			if(event.getItem().getItem() instanceof ItemFood){
				for (Ability ability : Ability.getAbilities(event.getEntityLiving()))
				{
					if(ability instanceof RestrictiveDiet && ability.isEnabled()){
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
