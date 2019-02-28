package com.nic.tfw.blocks.centrifuge;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.genes.GeneSet;
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

import static com.nic.tfw.superpower.genes.GeneSet.COLOR_TAG;

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
		else if (this.ticksRunning == 1)
		{
			ticksRunning = 0;
			if(!this.world.isRemote)
			{
				ItemStack stack1 = getStackInSlot(0);
				ItemStack stack2 = getStackInSlot(1);
				if (stack1.getItem() instanceof ItemVial && stack2.getItem() instanceof ItemVial)
				{
					GeneSet set1 = GeneSet.fromStack(stack1);
					GeneSet set2 = GeneSet.fromStack(stack2);

					GeneSet.SetType type1 = set1 != null ? set1.type : GeneSet.SetType.EMPTY;
					GeneSet.SetType type2 = set2 != null ? set2.type : GeneSet.SetType.EMPTY;

					if (type1 == GeneSet.SetType.GENE && type2 == GeneSet.SetType.GENE)
					{
						//Combine
						set1.mixSets(set2).addTo(stack2);
						setInventorySlotContents(0, new ItemStack(TheFifthWorld.Items.vial));
					}
					else if (type1 == GeneSet.SetType.GENE && type2 == GeneSet.SetType.EMPTY)
					{
						//Copy
						set1.addTo(stack2);
						stack2.getTagCompound().setIntArray(COLOR_TAG, stack1.getTagCompound().getIntArray(COLOR_TAG));
					}
					else if (type1 == GeneSet.SetType.GENE && type2 == GeneSet.SetType.SAMPLE)
					{
						//Finish
						setInventorySlotContents(0, new ItemStack(TheFifthWorld.Items.vial));
						set1.toSerum(set2).addTo(stack2);
					}
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

