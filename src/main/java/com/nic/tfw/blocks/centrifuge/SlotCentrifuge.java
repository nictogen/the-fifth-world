package com.nic.tfw.blocks.centrifuge;

import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.genes.GeneSet;
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
			GeneSet set1 = GeneSet.fromStack(stack1);
			GeneSet set2 = GeneSet.fromStack(stack2);
			GeneSet.SetType full1 = set1 != null ? set1.type : GeneSet.SetType.EMPTY;
			GeneSet.SetType full2 = set2 != null ? set2.type : GeneSet.SetType.EMPTY;

			if((full1 == GeneSet.SetType.GENE && full2 == GeneSet.SetType.GENE) || (full1 == GeneSet.SetType.GENE && full2 == GeneSet.SetType.EMPTY) || (full1 == GeneSet.SetType.GENE && full2 == GeneSet.SetType.SAMPLE))
			{
				centrifuge.ticksRunning = 100;
				centrifuge.markDirty();
				centrifuge.getWorld().notifyBlockUpdate(centrifuge.getPos(), centrifuge.getWorld().getBlockState(centrifuge.getPos()),
						centrifuge.getWorld().getBlockState(centrifuge.getPos()), 3);
			}
		}
	}
}
