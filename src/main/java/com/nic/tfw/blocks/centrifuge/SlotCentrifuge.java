package com.nic.tfw.blocks.centrifuge;

import com.nic.tfw.items.ItemVial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 1/12/19.
 */
public class SlotCentrifuge extends Slot
{
	private TileEntityCentrifuge centrifuge;
	public SlotCentrifuge(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		centrifuge = (TileEntityCentrifuge) inventoryIn;
	}

	public boolean isEnabled()
	{
		return centrifuge.ticksRunning == 0;
	}

	@Override public boolean isItemValid(ItemStack stack)
	{
		return centrifuge.ticksRunning == 0 && stack.getItem() instanceof ItemVial;
	}

	@Override public boolean canTakeStack(EntityPlayer playerIn)
	{
		return centrifuge.ticksRunning == 0;
	}

	@Override public void putStack(ItemStack stack)
	{
		super.putStack(stack);
		if(centrifuge.getWorld().isRemote) return;
		ItemStack stack1 = centrifuge.getStackInSlot(0);
		ItemStack stack2 = centrifuge.getStackInSlot(1);
		if(stack1.getItem() instanceof ItemVial && stack2.getItem() instanceof ItemVial){
			int full1 = stack1.hasTagCompound() ? stack1.getTagCompound().getInteger("full") : 0;
			int full2 = stack2.hasTagCompound() ? stack2.getTagCompound().getInteger("full") : 0;
			if((full1 == 2 && full2 == 2) || (full1 == 2 && full2 == 0) || (full1 == 2 && full2 == 1))
			{
				centrifuge.ticksRunning = 100;
				centrifuge.markDirty();
				centrifuge.getWorld().notifyBlockUpdate(centrifuge.getPos(), centrifuge.getWorld().getBlockState(centrifuge.getPos()),
						centrifuge.getWorld().getBlockState(centrifuge.getPos()), 3);
			}
		}
	}
}
