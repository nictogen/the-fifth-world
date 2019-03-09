package com.nic.tfw.client;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.blocks.centrifuge.ContainerCentrifuge;
import com.nic.tfw.blocks.centrifuge.TileEntityCentrifuge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Nictogen on 1/11/19.
 */
public class GuiCentrifuge extends GuiContainer
{
	private static final ResourceLocation centrifugeGuiTextures = new ResourceLocation(TheFifthWorld.MODID +":textures/gui/centrifuge_gui.png");
	private static final ResourceLocation centrifugeGuiTexture2 = new ResourceLocation(TheFifthWorld.MODID +":textures/gui/centrifuge_gui_2.png");
	private static final ResourceLocation centrifugeGuiTexture3 = new ResourceLocation(TheFifthWorld.MODID +":textures/gui/centrifuge_gui_3.png");

	private final IInventory tileCentrifuge;

	public GuiCentrifuge(InventoryPlayer parInventoryPlayer, IInventory parInventoryCentrifuge)
	{
		super(new ContainerCentrifuge(parInventoryPlayer, parInventoryCentrifuge));
		tileCentrifuge = parInventoryCentrifuge;
		ySize = 236;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(centrifugeGuiTextures);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0,
				xSize, ySize);

		if(tileCentrifuge instanceof TileEntityCentrifuge && ((TileEntityCentrifuge) tileCentrifuge).ticksRunning > 0)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(marginHorizontal + 88, marginVertical + 82, 0);
			GlStateManager.rotate((partialTicks + Minecraft.getMinecraft().player.ticksExisted) * 10, 0.0f, 0.0f, 1.0f);
			GlStateManager.translate(-(88 + marginHorizontal), -(82 + marginVertical), 0);
			mc.getTextureManager().bindTexture(centrifugeGuiTexture3);
			drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0,
					xSize, ySize);
			GlStateManager.popMatrix();
		} else {
			mc.getTextureManager().bindTexture(centrifugeGuiTexture2);
			drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0,
					xSize, ySize);
		}

	}

	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
