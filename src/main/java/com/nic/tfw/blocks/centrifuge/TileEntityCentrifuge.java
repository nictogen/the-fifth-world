package com.nic.tfw.blocks.centrifuge;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.genes.Gene;
import com.nic.tfw.superpower.genes.GeneHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
			ItemStack stack1 = getStackInSlot(0);
			ItemStack stack2 = getStackInSlot(1);
			if (stack1.getItem() instanceof ItemVial && stack2.getItem() instanceof ItemVial)
			{

				int full1 = stack1.hasTagCompound() ? stack1.getTagCompound().getInteger(GeneHandler.VIAL_TYPE_TAG) : 0;
				int full2 = stack2.hasTagCompound() ? stack2.getTagCompound().getInteger(GeneHandler.VIAL_TYPE_TAG) : 0;

				//Combine
				if (full1 == 2 && full2 == 2)
				{
					List<NBTTagCompound> uniqueAbilities = new ArrayList<>();
					HashSet<NBTTagCompound> defects = new HashSet<>();
					for (NBTBase nbtBase : stack1.getTagCompound().getTagList(GeneHandler.GENE_LIST_TAG, 10))
					{
						NBTTagCompound gene1Compound = (NBTTagCompound) nbtBase;

						boolean combine = false;

						for (NBTBase nbtBase2 : stack2.getTagCompound().getTagList(GeneHandler.GENE_LIST_TAG, 10))
						{
							NBTTagCompound gene2Compound = (NBTTagCompound) nbtBase2;
							if (!combine && gene2Compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG)
									.equals(gene1Compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG)))
							{
								for (NBTBase base : gene1Compound.getTagList(GeneHandler.DONOR_LIST_TAG, 8))
									for (NBTBase nbtBase1 : gene2Compound.getTagList(GeneHandler.DONOR_LIST_TAG, 8))
										if (base.toString().equals(nbtBase1.toString()))
											combine = true;

								if (!combine)
								{

									List<Gene> g = GeneHandler.GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene.getRegistryName().toString().equals(gene1Compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG))).collect(Collectors.toList());
									if(!g.isEmpty()){
										g.get(0).combineGenes(gene1Compound, gene2Compound);
									}
									float quality = gene2Compound.getFloat(GeneHandler.GENE_QUALITY_TAG) + gene1Compound.getFloat(GeneHandler.GENE_QUALITY_TAG);
									gene2Compound.setFloat(GeneHandler.GENE_QUALITY_TAG, quality);

									defects.addAll(GeneHandler.getDefects(GeneHandler.getRandom(stack1.getTagCompound(), stack2.getTagCompound()), quality));

									for (NBTBase base : gene1Compound.getTagList(GeneHandler.DONOR_LIST_TAG, 8))
										gene2Compound.getTagList(GeneHandler.DONOR_LIST_TAG, 8).appendTag(base);
									combine = true;
								}
							}
						}
						if (!combine)
						{
							uniqueAbilities.add(gene1Compound);
							defects.addAll(GeneHandler.getDefects(GeneHandler.getRandom(stack1.getTagCompound(), stack2.getTagCompound()),
									gene1Compound.getFloat(GeneHandler.GENE_QUALITY_TAG)));
						}
					}

					for (NBTTagCompound uniqueAbility : uniqueAbilities)
						stack2.getTagCompound().getTagList(GeneHandler.GENE_LIST_TAG, 10).appendTag(uniqueAbility);

					for (NBTBase nbtBase : stack1.getTagCompound().getTagList(GeneHandler.DONOR_LIST_TAG, 8))
						stack2.getTagCompound().getTagList(GeneHandler.DONOR_LIST_TAG, 8).appendTag(nbtBase);

					for (NBTBase nbtBase : stack1.getTagCompound().getTagList(GeneHandler.DEFECT_LIST_TAG, 10))
						defects.add((NBTTagCompound) nbtBase);

					if (stack2.getTagCompound().hasKey(GeneHandler.DEFECT_LIST_TAG))
						for (NBTBase nbtBase : stack2.getTagCompound().getTagList(GeneHandler.DEFECT_LIST_TAG, 10))
							defects.add((NBTTagCompound) nbtBase);

					NBTTagList dL = new NBTTagList();
					for (NBTTagCompound defect : defects)
					{
						dL.appendTag(defect);
					}

					stack2.getTagCompound().setTag(GeneHandler.DEFECT_LIST_TAG, dL);

					setInventorySlotContents(0, new ItemStack(TheFifthWorld.Items.vial));
				}
				//Copy
				else if (full1 == 2 && full2 == 0)
					stack2.setTagCompound(stack1.getTagCompound());
					//Finish
				else if (full1 == 2 && full2 == 1)
				{
					NBTTagCompound compound = stack1.getTagCompound();
					compound.setTag(GeneHandler.DONOR_LIST_TAG, stack2.getTagCompound().getTagList(GeneHandler.DONOR_LIST_TAG, 8));
					compound.setInteger(GeneHandler.VIAL_TYPE_TAG, 3);
					stack2.setTagCompound(compound);
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
