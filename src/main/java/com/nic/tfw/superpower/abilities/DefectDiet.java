package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectDiet extends AbilityConstant implements IDefect
{
	private AbilityCondition condition;

	public DefectDiet(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{

	}

	@Override public void setCondition(AbilityCondition condition)
	{
		this.condition = condition;
	}

	@Override public AbilityCondition getCondition()
	{
		return this.condition;
	}

	//TODO some available foods?
	@Mod.EventBusSubscriber
	public static class Handler {
		@SubscribeEvent
		public static void onEat(LivingEntityUseItemEvent event){
			if(event.getItem().getItem() instanceof ItemFood){
				for (Ability ability : Ability.getAbilities(event.getEntityLiving()))
				{
					if(ability instanceof DefectDiet && ((DefectDiet) ability).condition.test(ability)){
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
