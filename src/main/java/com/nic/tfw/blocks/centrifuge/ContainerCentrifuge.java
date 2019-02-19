package com.nic.tfw.blocks.centrifuge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 1/11/19.
 */
public class ContainerCentrifuge extends Container
{

	private final IInventory centrifugeInventory;

	public ContainerCentrifuge(IInventory playerInventory, IInventory centrifugeInventoryIn)
	{
		this.centrifugeInventory = centrifugeInventoryIn;

		this.addSlotToContainer(new SlotCentrifuge(centrifugeInventoryIn, 0, 46, 74));
		this.addSlotToContainer(new SlotCentrifuge(centrifugeInventoryIn, 1, 114, 74));

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 150 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 208));
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.centrifugeInventory.isUsableByPlayer(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
//		ItemStack itemstack = ItemStack.EMPTY;
//		Slot slot = this.inventorySlots.get(index);
//
//		if (slot != null && slot.getHasStack())
//		{
//			ItemStack itemstack1 = slot.getStack();
//			if(index > 1 && index <= 40){
//				if(mergeItemStack(itemstack1, 0, 2, false))
//					slot.putStack(ItemStack.EMPTY);
//			} else if(index == 0 || index == 1){
//				if(mergeItemStack(itemstack1, 2, 39, false))
//					slot.putStack(ItemStack.EMPTY);
//			}
//		}
//		return itemstack; TODO
		return ItemStack.EMPTY;
	}

}

