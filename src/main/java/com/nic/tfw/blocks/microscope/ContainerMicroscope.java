package com.nic.tfw.blocks.microscope;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 1/11/19.
 */
public class ContainerMicroscope extends Container
{

	private final IInventory microscopeInventory;

	public ContainerMicroscope(IInventory playerInventory, IInventory microscopeInventoryIn)
	{
		this.microscopeInventory = microscopeInventoryIn;

		this.addSlotToContainer(new SlotDNA(microscopeInventoryIn, 0, 6, 6));
		this.addSlotToContainer(new SlotGene(microscopeInventoryIn, 1, 30, 43));
		this.addSlotToContainer(new SlotGene(microscopeInventoryIn, 2, 90, 6));
		this.addSlotToContainer(new SlotGene(microscopeInventoryIn, 3, 140, 43));

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 63 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 121));
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.microscopeInventory.isUsableByPlayer(playerIn);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
//		ItemStack itemstack = ItemStack.EMPTY;
//		Slot slot = this.inventorySlots.get(index);
//
//		if (slot != null && slot.getHasStack())
//		{
//			ItemStack itemstack1 = slot.getStack(); TODO
//			if(index > 3 && index <= 40){
//				if(this.inventorySlots.get(0).getStack().isEmpty()){
//					this.inventorySlots.get(0).putStack(itemstack1);
//					slot.putStack(ItemStack.EMPTY);
//					return itemstack;
//				}
//			} else if(index == 0){
//				if(mergeItemStack(itemstack1, 3, 40, false))
//					slot.putStack(ItemStack.EMPTY);
//			}
//		}

//		return itemstack;
		return ItemStack.EMPTY;
	}

}
