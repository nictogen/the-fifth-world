package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectDiet extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectDiet(EntityPlayer player)
	{
		super(player);
	}

	@Override public void updateTick()
	{

	}

	@Override public void setCondition(Condition condition)
	{
		this.condition = condition;
	}

	@Override public Condition getCondition()
	{
		return this.condition;
	}

	//TODO some available foods?
	@Mod.EventBusSubscriber
	public static class Handler {
		@SubscribeEvent
		public static void onEat(LivingEntityUseItemEvent event){
			if(event.getItem().getItem() instanceof ItemFood && event.getEntity() instanceof EntityPlayer){
				SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler((EntityPlayer) event.getEntity());
				if(handler == null) return;
				for (Ability ability : handler.getAbilities())
				{
					if(ability instanceof DefectDiet && ((DefectDiet) ability).condition.isEnabled((EntityPlayer) event.getEntity(), ability)){
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
