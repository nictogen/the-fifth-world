package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataItemStack;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class AbilityChangeItem extends AbilityAction
{
	public static final AbilityData<ItemStack> FROM = new AbilityDataItemStack("from").disableSaving().setSyncType(EnumSync.SELF)
			.enableSetting("from", "The item to change from");
	public static final AbilityData<ItemStack> TO = new AbilityDataItemStack("to").disableSaving().setSyncType(EnumSync.SELF)
			.enableSetting("to", "The item to change to");

	public AbilityChangeItem(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().register(FROM, ItemStack.EMPTY);
		getDataManager().register(TO, ItemStack.EMPTY);
	}

	@Override public boolean action()
	{
		if (entity.getHeldItemMainhand().equals(getDataManager().get(FROM)))
		{
			entity.setHeldItem(EnumHand.MAIN_HAND, getDataManager().get(TO).copy());
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(getDataManager().get(TO), x + 2, y + 2);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(getDataManager().get(FROM), x - 2, y - 2);
		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}

	@Mod.EventBusSubscriber
	public static class Handler
	{
		@SubscribeEvent
		public static void onRightClick(PlayerInteractEvent.EntityInteract event)
		{
			if (event.getTarget() instanceof EntityLivingBase)
				for (Ability currentAbility : Ability.getAbilities((EntityLivingBase) event.getTarget()))
					if (currentAbility instanceof AbilityChangeItem && currentAbility.isUnlocked())
						if (((EntityLivingBase) event.getTarget()).getHeldItemMainhand().equals(currentAbility.getDataManager().get(FROM)))
						{
							((EntityLivingBase) event.getTarget()).setHeldItem(EnumHand.MAIN_HAND, currentAbility.getDataManager().get(TO).copy());
							currentAbility.setCooldown(currentAbility.getMaxCooldown());
						}
		}
	}
}
