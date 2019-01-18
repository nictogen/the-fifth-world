package com.nic.tfw.blocks.microscope;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Nictogen on 1/11/19.
 */
public class SlotDNA extends Slot
{
	public SlotDNA(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override public void onSlotChanged()
	{
		super.onSlotChanged();
		if(this.getStack().isEmpty()){
			inventory.setInventorySlotContents(1, ItemStack.EMPTY);
			inventory.setInventorySlotContents(2, ItemStack.EMPTY);
			inventory.setInventorySlotContents(3, ItemStack.EMPTY);
		} else {
			for (int i = 1; i < 4; i++)
			{
				NBTTagCompound c = this.getStack().hasTagCompound() ? this.getStack().getTagCompound().getCompoundTag("gene_" + i) : new NBTTagCompound();
				if(!c.isEmpty())
				{
					ItemStack s = new ItemStack(TheFifthWorld.Items.vial);
					NBTTagCompound geneCompound = new NBTTagCompound();
					geneCompound.setTag("gene_1", c);
					geneCompound.setInteger("full", 2);
					for (int j = 1; this.getStack().hasTagCompound() && this.getStack().getTagCompound().hasKey("defect_" + j); j++)
					{
						geneCompound.setTag("defect_" + j, this.getStack().getTagCompound().getCompoundTag("defect_" + j));
					}
					s.setTagCompound(geneCompound);
					inventory.setInventorySlotContents(i, s);
				} else inventory.setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
	}

	@Override public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() instanceof ItemVial && stack.hasTagCompound() && stack.getTagCompound().getInteger("full") == 1;
	}
}
