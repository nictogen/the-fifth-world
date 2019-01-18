package com.nic.tfw.client;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.blocks.centrifuge.ContainerCentrifuge;
import com.nic.tfw.blocks.microscope.ContainerMicroscope;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by Nictogen on 1/10/19.
 */
public class GuiHandler implements IGuiHandler
{
	@Nullable @Override public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null)
		{
			if (ID == TheFifthWorld.GUI.MICROSCOPE.ordinal())
			{
				return new ContainerMicroscope(player.inventory, (IInventory) tileEntity);
			} else if (ID == TheFifthWorld.GUI.CENTRIFUGE.ordinal())
			{
				return new ContainerCentrifuge(player.inventory, (IInventory) tileEntity);
			}
		}

		return null;
	}

	@Nullable @Override public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null)
		{
			if (ID == TheFifthWorld.GUI.MICROSCOPE.ordinal())
			{
				return new GuiMicroscope(player.inventory, (IInventory) tileEntity);
			} else if (ID == TheFifthWorld.GUI.CENTRIFUGE.ordinal())
			{
				return new GuiCentrifuge(player.inventory, (IInventory) tileEntity);
			}
		}

		return null;
	}
}
