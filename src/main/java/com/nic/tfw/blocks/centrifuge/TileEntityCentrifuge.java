package com.nic.tfw.blocks.centrifuge;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

/**
 * Created by Nictogen on 1/11/19.
 */
public class TileEntityCentrifuge extends TileEntityLockableLoot implements ITickable
{

	public int ticksRunning = 0;
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return 2;
	}

	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;

		return true;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (!this.checkLootAndRead(compound))
			ItemStackHelper.loadAllItems(compound, this.stacks);
		this.ticksRunning = compound.getInteger("ticksRunning");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (!this.checkLootAndWrite(compound))
			ItemStackHelper.saveAllItems(compound, this.stacks);
		compound.setInteger("ticksRunning", ticksRunning);
		return compound;
	}

	public int getInventoryStackLimit()
	{
		return 1;
	}

	public String getGuiID()
	{
		return TheFifthWorld.MODID + ":centrifuge";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		this.fillWithLoot(playerIn);
		return new ContainerCentrifuge(playerInventory, this);
	}

	protected NonNullList<ItemStack> getItems()
	{
		return this.stacks;
	}

	@Override public String getName()
	{
		return "container.centrifuge";
	}

	@Override public void update()
	{
		if (this.ticksRunning > 1)
			ticksRunning--;
		else
			if (this.ticksRunning == 1)
		{
			ticksRunning = 0;
			ItemStack stack1 = getStackInSlot(0);
			ItemStack stack2 = getStackInSlot(1);
			if (stack1.getItem() instanceof ItemVial && stack2.getItem() instanceof ItemVial)
			{

				int full1 = stack1.hasTagCompound() ? stack1.getTagCompound().getInteger("full") : 0;
				int full2 = stack2.hasTagCompound() ? stack2.getTagCompound().getInteger("full") : 0;

				//Combine
				if (full1 == 2 && full2 == 2)
				{
					for (int i = 1; stack1.getTagCompound().hasKey("gene_" + i); i++)
					{
						boolean combine = false;
						int i2 = 1;
						for (; stack2.getTagCompound().hasKey("gene_" + i2); i2++)
						{
							if (!combine && stack2.getTagCompound().getCompoundTag("gene_" + i2).getString("registry_name")
									.equals(stack1.getTagCompound().getCompoundTag("gene_" + i).getString("registry_name")))
							{
								if (stack2.getTagCompound().getCompoundTag("gene_" + i2).getUniqueId("donor_uuid").equals(stack1.getTagCompound().getCompoundTag("gene_" + i).getUniqueId("donor_uuid"))){
									combine = true;
									continue;
								}

								NBTTagCompound c = stack2.getTagCompound().getCompoundTag("gene_" + i2);
								c.setFloat("quality", (c.getFloat("quality") + stack1.getTagCompound().getCompoundTag("gene_" + i).getFloat("quality")) / 2.0f);
								c.setInteger("stacks", c.getInteger("stacks") + 1);
								stack2.getTagCompound().setTag("gene_" + i2, c);
								combine = true;
							}
						}
						if (!combine)
							stack2.getTagCompound().setTag("gene_" + i2, stack1.getTagCompound().getCompoundTag("gene_" + i));
					}

					for (int i = 1; stack1.getTagCompound().hasKey("defect_" + i); i++)
					{

						int i2 = 1;
						while (stack2.getTagCompound().hasKey("defect_" + i2))
						{
							i2++;
						}

						stack2.getTagCompound().setTag("defect_" + i2, stack1.getTagCompound().getCompoundTag("defect_" + i));
					}
					setInventorySlotContents(0, new ItemStack(TheFifthWorld.Items.vial));
				}
					//Copy
				else if (full1 == 2 && full2 == 0)
					stack2.setTagCompound(stack1.getTagCompound());
					//Finish
				else if (full1 == 2 && full2 == 1) {
					stack1.getTagCompound().setUniqueId("donor_uuid", stack2.getTagCompound().getUniqueId("donor_uuid"));
					stack1.getTagCompound().setInteger("full", 3);
					stack2.setTagCompound(stack1.getTagCompound());
					setInventorySlotContents(0, new ItemStack(TheFifthWorld.Items.vial));
				}
			}
		}
	}

	@Nullable @Override public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), 1, writeToNBT(new NBTTagCompound()));
	}

	@Override public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}
}

