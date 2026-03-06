package com.nic.tfw.client.gui.screen;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.menu.MicroscopeMenu;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticData;
import com.nic.tfw.genetics.GeneticUtil;
import com.nic.tfw.items.TFWItems;
import com.zigythebird.playeranim.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {

    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(TFWMod.MODID, "textures/gui/container/microscope.png");

    private static final int WIDTH = 256, HEIGHT = 231;

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
        if(!this.paused)
            timer++;
        if(this.focusedData != -1)
            this.focusedTime++;
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        this.paused = mouseX > this.leftPos + 10 && mouseX < this.leftPos + 181 && mouseY > this.topPos + 13 && mouseY < this.topPos + 137;
        var microscope = this.menu.getMicroscopeEntity(Minecraft.getInstance().level);
        if (microscope != null) {
            var geneticData = microscope.collectComponents().getOrDefault(TFWDataComponents.GENETIC_DATA, new ArrayList<GeneticData>());

            for (int i = 0; i < geneticData.size(); i++) {
                int y = (((i * 10) + timer) % 140);
                int x = (int) (3.0 * Math.sin((double) y / 3) + ( (((i * 10 + timer) / 140.0) % 9) * 20));

                int maxX = this.leftPos + Math.min(x + 5, 181);
                int maxY = this.topPos + Math.min(y + 5, 137);

                int minX = Math.min(this.leftPos + Math.max(x - 5, 10), maxX);
                int minY = Math.min(this.topPos + Math.max(y - 5, 13), maxY);
                boolean focused = mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
                if(!focused && focusedData == i){
                    focusedData = -1;
                    focusedTime = 0;
                }
                if(focused) {
                    if(focusedData != i){
                        focusedData = i;
                        focusedTime = 0;
                    }
                }
                guiGraphics.fill(minX, minY, maxX, maxY, geneticData.get(i).color().getRGB());
            }
            if(focusedData != -1) {
                int y = (((focusedData * 10) + timer) % 140);
                int x = (int) (3.0 * Math.sin((double) y / 3) + ((((focusedData * 10 + timer) / 140.0) % 9) * 20));
                int inflation = Math.min(10, 5 + focusedTime);
                var fData = geneticData.get(focusedData);
                guiGraphics.fill(
                        this.leftPos + x - inflation,
                        this.topPos + y - inflation,
                        this.leftPos + x + inflation,
                        this.topPos + y + inflation,
                        fData.color().getRGB());

                guiGraphics.drawString(this.font, GeneticUtil.getTitle(fData.geneType()), this.leftPos + 184, this.topPos + 12, fData.color().getRGB(), true);
                guiGraphics.drawWordWrap(this.font, GeneticUtil.getDesc(fData.geneType()), this.leftPos + 184, this.topPos + 25, 70, Color.WHITE.getRGB(), true);

                guiGraphics.drawWordWrap(this.font, fData.geneType().createValueComponent(fData.value()), this.leftPos + 184, this.topPos + 130, 70, fData.color().getRGB(), true);

            }
        }
    }
}
