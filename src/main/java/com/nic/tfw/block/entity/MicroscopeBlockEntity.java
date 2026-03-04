package com.nic.tfw.block.entity;

import com.mojang.serialization.Codec;
import com.nic.tfw.block.menu.MicroscopeMenu;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class MicroscopeBlockEntity extends BaseContainerBlockEntity {

    private List<GeneticData> geneticData = new ArrayList<>();
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public MicroscopeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TFWBlockEntities.MICROSCOPE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);

        this.geneticData.clear();
        this.geneticData.addAll(input.read("genetic_data", Codec.list(GeneticData.CODEC.codec())).orElse(List.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.store("genetic_data", Codec.list(GeneticData.CODEC.codec()), this.geneticData);
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
