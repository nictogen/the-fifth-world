package com.nic.tfw.items;

import com.mojang.serialization.Codec;
import com.nic.tfw.TFWMod;
import com.nic.tfw.component.TFWDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public enum GeneState implements StringRepresentable {
    EMPTY(0, "empty"),
    BLOOD(1, "blood"),
    DNA(2, "dna");
    private final String name;
    private final int id;
    public static final Codec<GeneState> CODEC = StringRepresentable.fromValues(GeneState::values);
    public static final IntFunction<GeneState> BY_ID = ByIdMap.continuous((gene) -> gene.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, GeneState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (gene) -> gene.id);

    GeneState(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Nullable
    public static GeneState getState(ItemStack item) {
        if (!item.has(TFWDataComponents.GENETIC_DATA))
            return null;
        var data = item.get(TFWDataComponents.GENETIC_DATA);
        if (data.isEmpty())
            return GeneState.EMPTY;
        return GeneState.BLOOD;
    }
}

