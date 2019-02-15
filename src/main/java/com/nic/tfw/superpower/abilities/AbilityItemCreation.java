package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityItemCreation extends AbilityAction
{
	private ItemStack item;
	public AbilityItemCreation(EntityLivingBase player, ItemStack item, int cooldown)
	{
		super(player);
		this.item = item;
		setMaxCooldown(cooldown);
	}

	@Override public boolean action()
	{
		entity.entityDropItem(item.copy(), 0f);
		return true;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(item, x, y);
	}
}
