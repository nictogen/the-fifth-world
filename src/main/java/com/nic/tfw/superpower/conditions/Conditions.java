package com.nic.tfw.superpower.conditions;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Nictogen on 1/16/19.
 */
@Mod.EventBusSubscriber
public class Conditions
{
	@GameRegistry.ObjectHolder(TheFifthWorld.MODID)
	public static class TheFifthWorldConditions {
		public static final AbilityCondition.ConditionEntry always_on = null;
		public static final AbilityCondition.ConditionEntry sneaking = null;
		public static final AbilityCondition.ConditionEntry falling = null;
		public static final AbilityCondition.ConditionEntry sleeping = null;
		public static final AbilityCondition.ConditionEntry sprinting = null;
		public static final AbilityCondition.ConditionEntry swimming = null;
		public static final AbilityCondition.ConditionEntry using_hand = null;
	}

	@SubscribeEvent
	public static void registerConditions(RegistryEvent.Register<AbilityCondition.ConditionEntry> e) {
		e.getRegistry().register(new AbilityCondition.ConditionEntry(AlwaysOnCondition.class, new ResourceLocation(TheFifthWorld.MODID, "always_on")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(SneakingCondition.class, new ResourceLocation(TheFifthWorld.MODID, "sneaking")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(FallingCondition.class, new ResourceLocation(TheFifthWorld.MODID, "falling")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(SleepingCondition.class, new ResourceLocation(TheFifthWorld.MODID, "sleeping")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(SprintingCondition.class, new ResourceLocation(TheFifthWorld.MODID, "sprinting")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(SwimmingCondition.class, new ResourceLocation(TheFifthWorld.MODID, "swimming")));
		e.getRegistry().register(new AbilityCondition.ConditionEntry(UsingHandCondition.class, new ResourceLocation(TheFifthWorld.MODID, "using_hand")));
	}

}

