package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityExplode extends AbilityAction
{
	public float strength;

	public AbilityExplode(EntityLivingBase player, float strength, int cooldown)
	{
		super(player);
		this.strength = strength;
		this.setMaxCooldown(cooldown);
	}

	@Override public boolean action()
	{
		entity.world.createExplosion(entity, entity.posX + 0.5, entity.posY + entity.getEyeHeight(), entity.posZ + 0.5, strength, false);
		return true;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.TNT), x, y);
	}
}
