package com.nic.tfw.client.gui.screen;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.menu.MicroscopeMenu;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.items.TFWItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {

    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(TFWMod.MODID, "textures/gui/container/microscope.png");

    private static final int WIDTH = 256, HEIGHT = 231;

    private int dnaStartX = 20, dnaStartY = 20;

    public MicroscopeScreen(MicroscopeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;

        this.titleLabelX = 10;
        this.titleLabelY = 3;

        this.inventoryLabelX = 8;
        this.inventoryLabelY = 141;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        //Render BG texture
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256
        );

        var pose = guiGraphics.pose();

        //Render Microscope Icon
        pose.pushMatrix();
        pose.translate(this.leftPos + 196, this.topPos + 195);
        pose.scale(1.9f);
        guiGraphics.renderFakeItem(new ItemStack(TFWItems.MICROSCOPE.asItem()), 0, 0);
        pose.popMatrix();
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);

        var dnaSlot = this.menu.getSlot(1);
        if(dnaSlot.hasItem() && dnaSlot.getItem().has(TFWDataComponents.GENETIC_DATA)){
            var geneticData = dnaSlot.getItem().get(TFWDataComponents.GENETIC_DATA);
            for (int i = 0; i < geneticData.size(); i++) {
                int y = this.dnaStartY + (i * 10);
                guiGraphics.fill(this.leftPos + this.dnaStartX - 5, this.topPos + y - 5, this.leftPos + this.dnaStartX + 5, this.topPos + y + 5, geneticData.get(i).color().getRGB());
            }
        }
    }
}
