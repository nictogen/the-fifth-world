package com.nic.tfw.block.menu;

import com.nic.tfw.block.entity.MicroscopeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MicroscopeMenu extends AbstractContainerMenu {

    private final Container container;
    private BlockPos entityPos;

    public MicroscopeMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf byteBuf) {
        this(containerId, inventory, new SimpleContainer(1));
        this.entityPos = byteBuf.readBlockPos();
    }

    public MicroscopeMenu(int containerId, Inventory inventory, Container container) {
        super(TFWMenus.MICROSCOPE_MENU.get(), containerId);
        this.container = container;
        this.addSlot(new Slot(container, 0, 204, 151));
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

    @Nullable
    public MicroscopeBlockEntity getMicroscopeEntity(Level level) {
        if(this.container instanceof MicroscopeBlockEntity microscopeEntity) return microscopeEntity;
        if(entityPos != null && level.getBlockEntity(entityPos) instanceof MicroscopeBlockEntity microscopeEntity) return microscopeEntity;
        return null;
    }
}
