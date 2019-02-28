package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class AbilityBonemealArea extends AbilityAction
{
	public static final AbilityData<Integer> RADIUS = new AbilityDataInteger("radius").disableSaving().setSyncType(EnumSync.SELF).enableSetting("radius", "The radius of the bonemeal effect.");

	public AbilityBonemealArea(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().register(RADIUS, 1);
	}

	@Override public boolean action()
	{
		for (int x = -getDataManager().get(RADIUS); x < getDataManager().get(RADIUS); x++)
			for (int z = -getDataManager().get(RADIUS); z < getDataManager().get(RADIUS); z++)
				for (int y = -1; y < 1; y++)
					ItemDye.applyBonemeal(ItemStack.EMPTY, entity.world, entity.getPosition().add(x, y, z));
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Items.DYE), x + 2, y + 2);
		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}
}
