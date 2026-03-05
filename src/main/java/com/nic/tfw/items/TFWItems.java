package com.nic.tfw.items;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.TFWBlocks;
import com.nic.tfw.component.TFWDataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;

public class TFWItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TFWMod.MODID);

    public static final DeferredItem<BlockItem> MICROSCOPE = ITEMS.registerSimpleBlockItem(
            "microscope",
            TFWBlocks.MICROSCOPE,
            properties -> properties
    );

    public static final DeferredItem<InjectionGunItem> INJECTION_GUN = ITEMS.registerItem(
            "injection_gun",
            InjectionGunItem::new
    );

    public static final DeferredItem<Item> VIAL = ITEMS.registerItem(
            "vial",
            ItemVial::new
    );
}
