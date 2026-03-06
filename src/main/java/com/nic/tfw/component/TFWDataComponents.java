package com.nic.tfw.component;

import com.mojang.serialization.Codec;
import com.nic.tfw.TFWMod;
import com.nic.tfw.genetics.GeneticData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class TFWDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TFWMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GeneticData>>> GENETIC_DATA = DATA_COMPONENTS.register("genetic_data", () -> DataComponentType.<List<GeneticData>>builder()
            .persistent(Codec.list(GeneticData.CODEC))
            .networkSynchronized(GeneticData.STREAM_CODEC.apply(ByteBufCodecs.list()))
            .build());
}
