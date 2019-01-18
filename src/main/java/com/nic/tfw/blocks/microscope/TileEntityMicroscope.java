package com.nic.tfw.blocks.microscope;

import com.nic.tfw.TheFifthWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;

/**
 * Created by Nictogen on 1/11/19.
 */
public class TileEntityMicroscope extends TileEntityLockableLoot
{

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return 4;
	}

	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.stacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (!this.checkLootAndRead(compound))
		{
			ItemStackHelper.loadAllItems(compound, this.stacks);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (!this.checkLootAndWrite(compound))
		{
			ItemStackHelper.saveAllItems(compound, this.stacks);
		}

		return compound;
	}

	public int getInventoryStackLimit()
	{
		return 4;
	}

	public String getGuiID()
	{
		return TheFifthWorld.MODID + ":microscope";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		this.fillWithLoot(playerIn);
		return new ContainerMicroscope(playerInventory, this);
	}

	protected NonNullList<ItemStack> getItems()
	{
		return this.stacks;
	}

	@Override public String getName()
	{
		return "container.microscope";
	}
}
