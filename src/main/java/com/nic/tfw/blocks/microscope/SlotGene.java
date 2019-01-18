package com.nic.tfw.blocks.microscope;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 1/11/19.
 */
public class SlotGene extends Slot
{
	public SlotGene(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override public boolean isItemValid(ItemStack stack)
	{
		return false;
	}

	@Override public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		inventory.setInventorySlotContents(0, ItemStack.EMPTY);
		inventory.setInventorySlotContents(1, ItemStack.EMPTY);
		inventory.setInventorySlotContents(2, ItemStack.EMPTY);
		inventory.setInventorySlotContents(3, ItemStack.EMPTY);
		return super.onTake(thePlayer, stack);
	}
}
