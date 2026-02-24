package com.nic.tfw.block.entity;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.TFWBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TFWBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TFWMod.MODID);

    public static final Supplier<BlockEntityType<MicroscopeBlockEntity>> MICROSCOPE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "microscope_block_entity",
            () -> new BlockEntityType<>(
                    MicroscopeBlockEntity::new,
                    false,
                    TFWBlocks.MICROSCOPE.get()
            ));
}
