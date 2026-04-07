package com.nic.tfw.block.entity;

import com.mojang.serialization.Codec;
import com.nic.tfw.block.menu.MicroscopeMenu;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticData;
import com.nic.tfw.items.VialState;
import com.nic.tfw.items.TFWItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MicroscopeBlockEntity extends BaseContainerBlockEntity {

    private List<GeneticData> geneticData = new ArrayList<>();
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    public int progress = 0;
    public static final int PIPETTE_TIME = 20;

    public MicroscopeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TFWBlockEntities.MICROSCOPE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level level, MicroscopeBlockEntity blockEntity) {
        if (level.isClientSide()) return;
        if (hasValidPipetteSlot(blockEntity)) {
            if (blockEntity.progress++ >= PIPETTE_TIME) {
                addPipetteSlot(blockEntity);
                blockEntity.progress = 0;
                ItemStack slot = blockEntity.getItem(0);
                slot.set(TFWDataComponents.LIQUID_AMOUNT, slot.getOrDefault(TFWDataComponents.LIQUID_AMOUNT, 1) - 1);
                if (slot.getOrDefault(TFWDataComponents.LIQUID_AMOUNT, 0) == 0)
                    blockEntity.setItem(0, new ItemStack(TFWItems.VIAL.asItem()));
                level.sendBlockUpdated(blockEntity.worldPosition, level.getBlockState(blockEntity.worldPosition), level.getBlockState(blockEntity.worldPosition), Block.UPDATE_CLIENTS);
            }
            blockEntity.setChanged();
        } else if (blockEntity.progress != 0) {
            blockEntity.progress = 0;
            blockEntity.setChanged();
        }
    }

    private static boolean hasValidPipetteSlot(MicroscopeBlockEntity blockEntity) {
        ItemStack slot = blockEntity.getItem(0);
        if (slot.isEmpty()) return false;
        if (slot.getOrDefault(TFWDataComponents.LIQUID_AMOUNT, 0) == 0) return false;
        VialState state = VialState.getState(blockEntity);
        VialState slotState = VialState.getState(slot);
        switch (state) {
            case EMPTY -> {
                return slotState != VialState.EMPTY;
            }
            case BLOOD -> {
                //Return true if an enzyme is in slot
                return false;
            }
            case DNA -> {
                //Return true if a usable enzyme is in slot
                return false;
            }
            case null -> {
                //Should never occur
                return false;
            }
            case ENZYME -> {
                return false;
            }
        }
    }

    private static void addPipetteSlot(MicroscopeBlockEntity blockEntity) {
        ItemStack slot = blockEntity.getItem(0);
        VialState state = VialState.getState(blockEntity);
        VialState slotState = VialState.getState(slot);
        switch (state) {
            case EMPTY -> {
                if (slotState != VialState.EMPTY) {
                    blockEntity.geneticData = slot.getOrDefault(TFWDataComponents.GENETIC_DATA, new ArrayList<>());
                    return;
                }
            }
            case BLOOD -> {
                //Return true if an enzyme is in slot
            }
            case DNA -> {
                //Return true if a usable enzyme is in slot
            }
            case null -> {
                //Should never occur
            }
            case ENZYME -> {
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.geneticData.clear();
        this.geneticData.addAll(input.read("genetic_data", Codec.list(GeneticData.CODEC)).orElse(List.of()));
        this.progress = input.getIntOr("progress", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.store("genetic_data", Codec.list(GeneticData.CODEC), this.geneticData);
        output.putInt("progress", this.progress);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.the_fifth_world.microscope_block_entity");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new MicroscopeMenu(i, inventory, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        super.writeClientSideData(menu, buffer);
        buffer.writeBlockPos(this.worldPosition);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        this.geneticData = dataComponentGetter.getOrDefault(TFWDataComponents.GENETIC_DATA.get(), new ArrayList<>());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(TFWDataComponents.GENETIC_DATA.get(), this.geneticData);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("genetic_data");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }
}
