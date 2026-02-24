package com.nic.tfw;

import com.mojang.logging.LogUtils;
import com.nic.tfw.block.TFWBlocks;
import com.nic.tfw.block.entity.TFWBlockEntities;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.genetypes.GeneType;
import com.nic.tfw.genetics.genetypes.GeneTypes;
import com.nic.tfw.items.TFWItems;
import com.nic.tfw.power.ability.TFWAbilitySerializers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod(value = TFWMod.MODID)
@EventBusSubscriber(modid = TFWMod.MODID)
public class TFWMod {
    public static final String MODID = "the_fifth_world";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFWMod(IEventBus modEventBus, ModContainer modContainer) {
        TFWBlocks.BLOCKS.register(modEventBus);
        TFWBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        TFWItems.ITEMS.register(modEventBus);
        GeneTypes.GENE_TYPES.register(modEventBus);
        TFWAbilitySerializers.ABILITIES.register(modEventBus);
        TFWDataComponents.DATA_COMPONENTS.register(modEventBus);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(GeneType.GENE_TYPE_REGISTRY);
    }
}
