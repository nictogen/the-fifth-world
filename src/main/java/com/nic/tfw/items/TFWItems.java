package com.nic.tfw.items;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.TFWBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TFWItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TFWMod.MODID);

    public static final DeferredItem<BlockItem> MICROSCOPE = ITEMS.registerSimpleBlockItem(
            "microscope",
            TFWBlocks.MICROSCOPE,
            properties -> properties
    );
}
