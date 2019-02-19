package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataString;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class AbilitySummonEntity extends AbilityAction
{
	public static AbilityData<String> ENTITY_NAME = new AbilityDataString("entity").disableSaving().setSyncType(EnumSync.SELF).enableSetting("entity", "The name of the entity to spawn.");
	public static AbilityData<Integer> NUMBER = new AbilityDataInteger("number").disableSaving().setSyncType(EnumSync.SELF).enableSetting("number", "The number of entities to spawn.");

	public AbilitySummonEntity(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().register(ENTITY_NAME, "minecraft:zombie");
		getDataManager().register(NUMBER, 1);
	}

	@Override public boolean action()
	{
		for (int i = 0; i < getDataManager().get(NUMBER); i++)
			ItemMonsterPlacer.spawnCreature(entity.world, new ResourceLocation(getDataManager().get(ENTITY_NAME)), entity.posX, entity.posY, entity.posZ);
		return true;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
		ItemStack stack = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(stack, new ResourceLocation(getDataManager().get(ENTITY_NAME)));
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(stack, x, y);
		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}
}
