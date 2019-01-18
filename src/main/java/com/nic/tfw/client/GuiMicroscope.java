package com.nic.tfw.client;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.blocks.microscope.ContainerMicroscope;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Nictogen on 1/10/19.
 */
public class GuiMicroscope extends GuiContainer
{
	private static final ResourceLocation microscopeGuiTextures = new ResourceLocation(TheFifthWorld.MODID +":textures/gui/microscope_gui.png");

	private final InventoryPlayer inventoryPlayer;
	private final IInventory tileMicroscope;

	public GuiMicroscope(InventoryPlayer parInventoryPlayer, IInventory parInventoryMicroscope)
	{
		super(new ContainerMicroscope(parInventoryPlayer, parInventoryMicroscope));
		inventoryPlayer = parInventoryPlayer;
		tileMicroscope = parInventoryMicroscope;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(microscopeGuiTextures);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0,
				xSize, ySize - 22);
		drawTexturedModalRect(marginHorizontal + 27, marginVertical + 5, 0, 143,
				140, 55);

		float speed = 10f;
		if(!tileMicroscope.isEmpty())
		{
			drawTexturedModalRect(marginHorizontal + 27, marginVertical + 22, 109 - (int) ((System.currentTimeMillis() % (109.0*speed)) / speed), 198,
					(int) ((System.currentTimeMillis() % (109.0*speed)) / speed), 21);
			drawTexturedModalRect(marginHorizontal + 27 + (int) ((System.currentTimeMillis() % (109.0*speed)) / speed), marginVertical + 22, 0, 198,
					109 - (int) ((System.currentTimeMillis() % (109.0*speed)) / speed), 21);
			drawTexturedModalRect(marginHorizontal + 136, marginVertical + 22, 109 -(int) ((System.currentTimeMillis() % (109.0*speed)) / speed), 198,
					30, 21);

		}
	}

	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
