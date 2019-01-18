package com.nic.tfw.superpower.conditions;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by Nictogen on 1/16/19.
 */
@Mod.EventBusSubscriber
public abstract class Condition extends IForgeRegistryEntry.Impl<Condition>
{
	public static IForgeRegistry<Condition> CONDITION_REGISTRY;
	public abstract boolean isEnabled(EntityPlayer player, Ability ability);

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event)
	{
		CONDITION_REGISTRY = new RegistryBuilder<Condition>().setName(new ResourceLocation(LucraftCore.MODID, "condition")).setType(Condition.class).setIDRange(0, 512).create();
	}

	public static void populateConditionList()
	{
		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return true;
			}
		}.setRegistryName(TheFifthWorld.MODID, "alwaysOn"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.isSneaking();
			}
		}.setRegistryName(TheFifthWorld.MODID, "isSneaking"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.isPlayerSleeping();
			}
		}.setRegistryName(TheFifthWorld.MODID, "isSleeping"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.isInWater();
			}
		}.setRegistryName(TheFifthWorld.MODID, "inWater"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.isSprinting();
			}
		}.setRegistryName(TheFifthWorld.MODID, "isSprinting"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.fallDistance > 1;
			}
		}.setRegistryName(TheFifthWorld.MODID, "isFalling"));

		CONDITION_REGISTRY.register(new Condition()
		{
			@Override public boolean isEnabled(EntityPlayer player, Ability ability)
			{
				return player.isHandActive();
			}
		}.setRegistryName(TheFifthWorld.MODID, "handActive"));

		CONDITION_REGISTRY.register(new JumpCondition().setRegistryName("onJump"));

		CONDITION_REGISTRY.register(new UsePowerCondition().setRegistryName("onUsePower"));
	}


}

