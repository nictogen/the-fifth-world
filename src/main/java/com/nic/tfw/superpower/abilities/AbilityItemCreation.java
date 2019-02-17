package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataItemStack;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityItemCreation extends AbilityAction
{
	public static AbilityData<ItemStack> ITEM = new AbilityDataItemStack("item").disableSaving().setSyncType(EnumSync.SELF).enableSetting("item", "Sets the itemstack to create");

	public AbilityItemCreation(EntityLivingBase player, ItemStack item, int cooldown)
	{
		super(player);
		setMaxCooldown(cooldown);
		dataManager.set(ITEM, item);
	}

	@Override
	public void registerData()
	{
		super.registerData();
		this.dataManager.register(ITEM, ItemStack.EMPTY);
	}

	@Override public boolean action()
	{
		entity.entityDropItem(this.dataManager.get(ITEM).copy(), 0f);
		return true;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(this.dataManager.get(ITEM), x, y);
	}
}
