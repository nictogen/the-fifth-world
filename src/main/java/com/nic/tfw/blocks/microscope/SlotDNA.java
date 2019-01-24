package com.nic.tfw.blocks.microscope;

import com.google.common.collect.Lists;
import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.genes.GeneSet;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
			GeneSet g = GeneSet.fromStack(getStack());

			if(g != null){
				for (int i = 0; i < g.genes.size() && i < 4; i++)
				{
					ItemStack s = new ItemStack(TheFifthWorld.Items.vial);
					new GeneSet(GeneSet.SetType.GENE, Lists.newArrayList(g.genes.get(i)), g.defects).addTo(s);
					inventory.setInventorySlotContents(i + 1, s);
				}
			}
		}
	}

	@Override public boolean isItemValid(ItemStack stack)
	{
		GeneSet g = GeneSet.fromStack(stack);
		return stack.getItem() instanceof ItemVial && g != null && g.type == GeneSet.SetType.SAMPLE;
	}
}
