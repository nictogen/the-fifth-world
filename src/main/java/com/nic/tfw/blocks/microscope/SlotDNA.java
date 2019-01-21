package com.nic.tfw.blocks.microscope;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.genes.GeneHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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

			if(getStack().hasTagCompound()){
				int i = 1;
				for (NBTBase nbtBase : getStack().getTagCompound().getTagList(GeneHandler.GENE_LIST_TAG, 10))
				{
					if(i < 4)
					{
						NBTTagCompound compound = new NBTTagCompound();
						ItemStack s = new ItemStack(TheFifthWorld.Items.vial);
						compound.setInteger(GeneHandler.VIAL_TYPE_TAG, 2);
						NBTTagList list = new NBTTagList();
						list.appendTag(nbtBase);
						compound.setTag(GeneHandler.GENE_LIST_TAG, list);
						compound.setTag(GeneHandler.DONOR_LIST_TAG, ((NBTTagCompound)nbtBase).getTagList(GeneHandler.DONOR_LIST_TAG, 8));
						s.setTagCompound(compound);
						inventory.setInventorySlotContents(i, s);
						i++;
					}
//					for (int j = 1; this.getStack().hasTagCompound() && this.getStack().getTagCompound().hasKey("defect_" + j); j++)
//					{
//						geneCompound.setTag("defect_" + j, this.getStack().getTagCompound().getCompoundTag("defect_" + j));
//					}
				}
			}
		}
	}

	@Override public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() instanceof ItemVial && stack.hasTagCompound() && stack.getTagCompound().getInteger("full") == 1;
	}
}
