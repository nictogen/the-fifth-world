package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataBlock;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataFloat;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityEatBlock extends AbilityAction
{
	public static final AbilityData<Float> SATURATION = new AbilityDataFloat("saturation").disableSaving().setSyncType(EnumSync.SELF).enableSetting("saturation", "The amount of saturation to gain from eating the block.");
	public static final AbilityData<Integer> FOOD = new AbilityDataInteger("food").disableSaving().setSyncType(EnumSync.SELF).enableSetting("food", "The amount of food to gain from eating the block.");
	public static final AbilityData<Block> FROM = new AbilityDataBlock("from").disableSaving().setSyncType(EnumSync.SELF).enableSetting("from", "The block to eat");
	public static final AbilityData<Block> TO = new AbilityDataBlock("to").disableSaving().setSyncType(EnumSync.SELF).enableSetting("to", "The block to turn eaten block in to");

	public AbilityEatBlock(EntityLivingBase player)
	{
		super(player);
	}

	@Override public boolean action()
	{
		Vec3d lookVec = entity.getLookVec().scale(2f);
		RayTraceResult rtr = entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
				new Vec3d(entity.posX + lookVec.x, entity.posY + entity.getEyeHeight() + lookVec.y, entity.posZ + lookVec.z));
		if(rtr != null && entity.world.getBlockState(rtr.getBlockPos()).getBlock() == getDataManager().get(FROM)){
			entity.world.setBlockState(rtr.getBlockPos(), getDataManager().get(TO).getDefaultState());
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).getFoodStats().addStats(getDataManager().get(FOOD), getDataManager().get(SATURATION));
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(getDataManager().get(FROM)), x, y);
		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}
}
