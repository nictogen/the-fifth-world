package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.GeneSet;
import net.minecraft.entity.EntityLivingBase;
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

/**
 * Created by Nictogen on 1/14/19.
 */
public class ItemInjectionGun extends Item
{
	public ItemInjectionGun()
	{
		setRegistryName(TheFifthWorld.MODID, "injection_gun");
		setTranslationKey("injection_gun");
		addPropertyOverride(new ResourceLocation("full"), (stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneSet.VIAL_DATA_TAG) ? stack.getTagCompound().getCompoundTag(
				GeneSet.VIAL_DATA_TAG).getInteger(GeneSet.VIAL_TYPE_TAG) : -1);
		setMaxStackSize(1);
	}

	@Override public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (entityLiving.isSneaking() && entityLiving instanceof EntityPlayerMP)
		{
			GeneSet existingGeneSet = GeneSet.fromStack(stack);
			if(existingGeneSet == null) return super.onEntitySwing(entityLiving, stack);

			if (existingGeneSet.type == GeneSet.SetType.EMPTY)
			{
				new GeneSet(entityLiving).addTo(stack);
				return true;
			}
			else if(existingGeneSet.type == GeneSet.SetType.SERUM){
				if(existingGeneSet.giveTo(entityLiving, (EntityPlayer) entityLiving))
					removeContentsOfVial(stack);
			}
			return true;
		}
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		GeneSet existingGeneSet = GeneSet.fromStack(stack);
		if(attacker.world.isRemote || existingGeneSet == null) return super.hitEntity(stack, target, attacker);
		if (existingGeneSet.type == GeneSet.SetType.EMPTY)
			new GeneSet(target).addTo(stack);
		else if(existingGeneSet.type == GeneSet.SetType.SERUM){
			if(existingGeneSet.giveTo(target, (attacker instanceof EntityPlayer) ? (EntityPlayer) attacker : null))
				removeContentsOfVial(stack);
		}
		return super.hitEntity(stack, target, attacker);
	}

	public static void removeContentsOfVial(ItemStack stack){
		stack.getTagCompound().setTag(GeneSet.VIAL_DATA_TAG, new GeneSet(new NBTTagCompound()).serializeNBT());
	}

	@Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(playerIn.world.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);
		EnumHand off = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = playerIn.getHeldItem(handIn);
		ItemStack stackOff = playerIn.getHeldItem(off);

		GeneSet set = GeneSet.fromStack(stack);
		GeneSet setOff = GeneSet.fromStack(stackOff);

		if (stackOff.getItem() instanceof ItemVial && set == null)
		{
			if (setOff == null || setOff.type == GeneSet.SetType.EMPTY)
			{
				(setOff != null ? setOff : new GeneSet(new NBTTagCompound())).addTo(stack);
				playerIn.setHeldItem(off, ItemStack.EMPTY);
			} else if(setOff.type != GeneSet.SetType.GENE) {
				setOff.addTo(stack);
				playerIn.setHeldItem(off, ItemStack.EMPTY);
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		else if (stackOff.isEmpty() && set != null)
		{
			ItemStack vial = new ItemStack(TheFifthWorld.Items.vial);
			set.addTo(vial);
			playerIn.setHeldItem(off, vial);
			stack.getTagCompound().removeTag(GeneSet.VIAL_DATA_TAG);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
