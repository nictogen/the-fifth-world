package com.nic.tfw.client.gui.screen;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.menu.MicroscopeMenu;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticData;
import com.nic.tfw.genetics.GeneticUtil;
import com.nic.tfw.items.TFWItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {

    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(TFWMod.MODID, "textures/gui/container/microscope.png");
    private static final Identifier DNA_SPRITES = Identifier.fromNamespaceAndPath(TFWMod.MODID, "textures/gui/container/dna_sprites.png");

    private static final int WIDTH = 256, HEIGHT = 231, DNA_WIDTH = 7, DNA_HEIGHT = 21;

    private int timer = 0, focusedData = -1, focusedTime = 0;
    private boolean paused = false;

    public MicroscopeScreen(MicroscopeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;

        this.titleLabelX = 10;
        this.titleLabelY = 2;

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
    protected void containerTick() {
        if (!this.paused)
            timer++;
        if (this.focusedData != -1)
            this.focusedTime++;
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);

        this.paused = mouseX > this.leftPos + 10 && mouseX < this.leftPos + 181 && mouseY > this.topPos + 13 && mouseY < this.topPos + 137;

        var microscope = this.menu.getMicroscopeEntity(Minecraft.getInstance().level);
        if (microscope != null) {

            guiGraphics.enableScissor(this.leftPos + 10, this.topPos + 13, this.leftPos + 181, this.topPos + 137);

            var geneticData = microscope.collectComponents().getOrDefault(TFWDataComponents.GENETIC_DATA, new ArrayList<GeneticData>());
            boolean focused = false;
            for (int i = 0; i < geneticData.size(); i++) {

                int x = this.leftPos + ((int) (((i * 21.0 + timer)) / 160) % 12) * 14 + 15;
                int y = this.topPos + ((i * 21 + timer) % 160) - 10;

                if(!focused) {
                    focused = this.paused && mouseX >= x - 3.5 && mouseX <= x - 3.5 + DNA_WIDTH && mouseY >= y - 10.5 && mouseY <= y - 10.5 + DNA_HEIGHT;

                    if(focused){
                        if(focusedData != i) {
                            focusedData = i;
                            focusedTime = 0;
                        }
                    } else {
                        renderDNA(guiGraphics, 1.0f, x, y, geneticData, i);
                        if(focusedData == i) focusedData = -1;
                    }
                } else renderDNA(guiGraphics, 1.0f, x, y, geneticData, i);
            }

            guiGraphics.disableScissor();

            if (focusedData != -1) {
                int x = this.leftPos + ((int) (((focusedData * 21.0 + timer)) / 160) % 12) * 14 + 15;
                int y = this.topPos + ((focusedData * 21 + timer) % 160) - 10;
                float inflation = Math.min(20, 5 + focusedTime) * 0.2f;
                renderDNA(guiGraphics, inflation, x, y, geneticData, focusedData);
                var fData = geneticData.get(focusedData);

                guiGraphics.drawString(this.font, GeneticUtil.getTitle(fData.geneType()), this.leftPos + 184, this.topPos + 12, fData.color1().getRGB(), true);
                guiGraphics.drawWordWrap(this.font, GeneticUtil.getDesc(fData.geneType()), this.leftPos + 184, this.topPos + 25, 70, Color.WHITE.getRGB(), true);

                guiGraphics.drawWordWrap(this.font, fData.geneType().createValueComponent(fData.value()), this.leftPos + 184, this.topPos + 130, 70, fData.color2().getRGB(), true);

            }
        }
    }

    private void renderDNA(GuiGraphics guiGraphics, float scale, int x, int y, List<GeneticData> data, int i) {
        var pose = guiGraphics.pose();
        int uOff = (data.size() == 1 || scale >= 2.0f) ? 0 : (data.size() == i + 1) ? 7 : (i == 0) ? 14 : 21;

        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale);
        pose.translate(-3.5f, -10.5f);
        //Color 1
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                DNA_SPRITES,
                0, 0,
                uOff, 0,
                DNA_WIDTH, DNA_HEIGHT,
                256, 256,
                data.get(i).color1().getRGB()
        );
        //Color 2
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                DNA_SPRITES,
                0, 0,
                28 + uOff, 0,
                DNA_WIDTH, DNA_HEIGHT,
                256, 256,
                data.get(i).color2().getRGB()
        );
        //Frame
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                DNA_SPRITES,
                0, 0,
                i % 2 * 28 + uOff, 21,
                DNA_WIDTH, DNA_HEIGHT,
                256, 256
        );
        pose.popMatrix();
    }
}
