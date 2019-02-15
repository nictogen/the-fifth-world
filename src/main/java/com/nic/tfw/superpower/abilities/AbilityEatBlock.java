package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityEatBlock extends AbilityAction
{
	private IBlockState fromState = Blocks.AIR.getDefaultState();
	private IBlockState toState = Blocks.AIR.getDefaultState();

	public int getHungerIncrease()
	{
		return hungerIncrease;
	}

	public float getSaturationIncrease()
	{
		return saturationIncrease;
	}

	private int hungerIncrease = 0;
	private float saturationIncrease = 0f;

	public AbilityEatBlock(EntityLivingBase player)
	{
		super(player);
	}

	public AbilityEatBlock(EntityLivingBase player, IBlockState fromState, IBlockState toState, int hungerIncrease, float saturationIncrease)
	{
		super(player);
		this.fromState = fromState;
		this.toState = toState;
		this.hungerIncrease = hungerIncrease;
		this.saturationIncrease = saturationIncrease;
	}

	@Override public boolean action()
	{
		Vec3d lookVec = entity.getLookVec().scale(2f);
		RayTraceResult rtr = entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
				new Vec3d(entity.posX + lookVec.x, entity.posY + entity.getEyeHeight() + lookVec.y, entity.posZ + lookVec.z));
		if(rtr != null && entity.world.getBlockState(rtr.getBlockPos()).getBlock() == fromState.getBlock()){
			entity.world.setBlockState(rtr.getBlockPos(), toState);
			if(entity instanceof EntityPlayer)
				((EntityPlayer) entity).getFoodStats().addStats(hungerIncrease, saturationIncrease);
			return true;
		}
		return false;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		//TODO check if works
//		float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
//		Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(x, y, 0);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(fromState.getBlock()), x, y);
//		GlStateManager.popMatrix();
//		Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
	}
}
