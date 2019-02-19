package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataFloat;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityExplode extends AbilityAction
{
	public static final AbilityData<Float> STRENGTH = new AbilityDataFloat("strength").disableSaving().setSyncType(EnumSync.SELF).enableSetting("strength", "The strength of the explosion");

	public AbilityExplode(EntityLivingBase player)
	{
		super(player);
	}

	@Override
	public void registerData()
	{
		super.registerData();
		this.dataManager.register(STRENGTH, 1F);
	}

	@Override public boolean action()
	{
		entity.world.createExplosion(entity, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, this.dataManager.get(STRENGTH), true);
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.TNT), x, y);
	}
}
