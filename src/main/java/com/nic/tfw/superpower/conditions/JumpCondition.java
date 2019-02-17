//package com.nic.tfw.superpower.conditions;
//
//import com.nic.tfw.superpower.abilities.IDefect;
//import lucraft.mods.lucraftcore.superpowers.SuperpowerEntityHandler;
//import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
//import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
///**
// * Created by Nictogen on 1/17/19.
// */
//@Mod.EventBusSubscriber
//public class JumpCondition extends OneUseCondition
//{
//
//	@SubscribeEvent
//	public static void onJump(LivingEvent.LivingJumpEvent event)
//	{
//		SuperpowerEntityHandler handler = SuperpowerHandler.getSuperpowerEntityHandler( event.getEntityLiving());
//		if (handler == null)
//			return;
//		for (Ability ability : handler.getAbilities())
//		{
//			if (ability instanceof IDefect)
//			{
//				if (((IDefect) ability).getCondition() instanceof JumpCondition)
//				{
//					((JumpCondition) ((IDefect) ability).getCondition()).setEnabled(event.getEntityLiving());
//				}
//			}
//		}
//
//	}
//}
