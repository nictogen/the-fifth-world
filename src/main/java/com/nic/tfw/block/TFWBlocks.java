package com.nic.tfw.block;

import com.nic.tfw.TFWMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TFWBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TFWMod.MOD_ID);

    public static final DeferredBlock<BlockMicroscope> MICROSCOPE = BLOCKS.register(
            "microscope",
            registryName -> new BlockMicroscope(
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, registryName))
            )
    );
}
