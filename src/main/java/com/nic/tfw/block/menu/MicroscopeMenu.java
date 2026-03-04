package com.nic.tfw.block.menu;

import com.nic.tfw.block.menu.slot.MicroscopeSlideSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MicroscopeMenu extends AbstractContainerMenu {

    private final Container container;

    public MicroscopeMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(2));
    }

    public MicroscopeMenu(int containerId, Inventory inventory, Container container) {
        super(TFWMenus.MICROSCOPE_MENU.get(), containerId);
        this.container = container;
        this.addSlot(new MicroscopeSlideSlot(container, 0, 11, 12));
        this.addSlot(new Slot(container, 1, 204, 151));
        this.addStandardInventorySlots(inventory, 9, 151);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY; //TODO
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
