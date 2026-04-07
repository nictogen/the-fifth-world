package com.nic.tfw.items;

import com.mojang.serialization.Codec;
import com.nic.tfw.component.TFWDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public enum VialState implements StringRepresentable {
    EMPTY(0, "empty"),
    BLOOD(1, "blood"),
    DNA(2, "dna"),
    ENZYME(3, "enzyme");
    private final String name;
    private final int id;
    public static final Codec<VialState> CODEC = StringRepresentable.fromValues(VialState::values);
    public static final IntFunction<VialState> BY_ID = ByIdMap.continuous((gene) -> gene.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, VialState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (gene) -> gene.id);

    VialState(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Nullable
    private static VialState getState(DataComponentMap geneticSource) {
        var data = geneticSource.get(TFWDataComponents.GENETIC_DATA);

        if(data == null) return null;
        if (data.isEmpty())
            return VialState.EMPTY;
        return VialState.BLOOD;
    }

    @Nullable
    public static VialState getState(ItemStack stack){
        return getState(stack.getComponents());
    }

    @Nullable
    public static VialState getState(BlockEntity blockEntity) {
        return getState(blockEntity.collectComponents());
    }
}

