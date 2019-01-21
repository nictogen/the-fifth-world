package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.GeneHandler;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Random;

/**
 * Created by Nictogen on 1/14/19.
 */
public class ItemInjectionGun extends Item
{
	public ItemInjectionGun()
	{
		setRegistryName(TheFifthWorld.MODID, "injection_gun");
		setTranslationKey("injection_gun");
		addPropertyOverride(new ResourceLocation("full"), (stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG) ? stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getInteger(GeneHandler.VIAL_TYPE_TAG) : -1);
		setMaxStackSize(1);
	}

	@Override public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (entityLiving.isSneaking() && entityLiving instanceof EntityPlayerMP)
		{
			if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG)) return super.onEntitySwing(entityLiving, stack);
			int full = stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getInteger(GeneHandler.VIAL_TYPE_TAG);
			if (full == 0)
			{
				GeneHandler.getRandomGenes(entityLiving, stack);
				return true;
			}
			else if(full == 3){
				String uuid = stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getTagList(GeneHandler.DONOR_LIST_TAG, 8).getStringTagAt(0);
				if(entityLiving.getPersistentID().toString().equals(uuid)){
					GeneHandler.giveSuperpowerFromInjectionGun(stack, (EntityPlayer) entityLiving);
				}
			}
			return true;
		}
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(attacker.world.isRemote || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG)) return super.hitEntity(stack, target, attacker);
		int full = stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getInteger(GeneHandler.VIAL_TYPE_TAG);
		if (full == 0)
		{
			GeneHandler.getRandomGenes(target, stack);
		}
		else if(full == 3){

			String uuid = stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getTagList(GeneHandler.DONOR_LIST_TAG, 8).getStringTagAt(0);
			if(!target.getPersistentID().toString().equals(uuid)) return super.hitEntity(stack, target, attacker);
			if(target instanceof EntityPlayer) {
				GeneHandler.giveSuperpowerFromInjectionGun(stack, (EntityPlayer) target);
			} else if(target instanceof EntityVillager){
				GeneHandler.giveSuperpowerFromInjectionGun(stack, attacker, (EntityVillager) target);
			}
		}
		return super.hitEntity(stack, target, attacker);
	}

	public static void removeContentsOfVial(ItemStack stack){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(GeneHandler.VIAL_TYPE_TAG, 0);
		stack.getTagCompound().setTag(GeneHandler.VIAL_DATA_TAG, compound);
	}

	@Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(playerIn.world.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);
		EnumHand off = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = playerIn.getHeldItem(handIn);
		ItemStack stackOff = playerIn.getHeldItem(off);
		if (stackOff.getItem() instanceof ItemVial && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG)))
		{
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			if (!stackOff.hasTagCompound() || stackOff.getTagCompound().getInteger(GeneHandler.VIAL_TYPE_TAG) == 0)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger(GeneHandler.VIAL_TYPE_TAG, 0);
				stackOff.setTagCompound(nbt);
			}
			else if (stackOff.hasTagCompound() && stackOff.getTagCompound().getInteger(GeneHandler.VIAL_TYPE_TAG) == 2)
				return super.onItemRightClick(worldIn, playerIn, handIn);
			stack.getTagCompound().setTag(GeneHandler.VIAL_DATA_TAG, stackOff.getTagCompound());
			playerIn.setHeldItem(off, ItemStack.EMPTY);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		else if (stackOff.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG))
		{
			ItemStack vial = new ItemStack(TheFifthWorld.Items.vial);
			vial.setTagCompound(stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG));
			playerIn.setHeldItem(off, vial);
			stack.getTagCompound().removeTag(GeneHandler.VIAL_DATA_TAG);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public static class InjectionGeneColor implements IItemColor
	{
		@Override public int colorMultiplier(ItemStack stack, int tintIndex)
		{
			Color c = new Color(1.0f, 1.0f, 1.0f, 1.0f);

			if (tintIndex == 1 && stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneHandler.VIAL_DATA_TAG))
			{
				Random r = GeneHandler.getRandom(stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG));
				c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
			}
			return c.getRGB();
		}
	}


}
