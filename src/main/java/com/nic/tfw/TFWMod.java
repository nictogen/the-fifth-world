package com.nic.tfw;

import com.nic.tfw.block.TFWBlocks;
import com.nic.tfw.block.entity.TFWBlockEntities;
import com.nic.tfw.block.menu.TFWMenus;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.genetypes.GeneType;
import com.nic.tfw.genetics.genetypes.GeneTypes;
import com.nic.tfw.items.TFWItems;
import com.nic.tfw.power.ability.TFWAbilitySerializers;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(value = TFWMod.MOD_ID)
@EventBusSubscriber(modid = TFWMod.MOD_ID)
public class TFWMod {
    public static final String MOD_ID = "the_fifth_world";

    public TFWMod(IEventBus modEventBus, ModContainer ignoredModContainer) {
        TFWBlocks.BLOCKS.register(modEventBus);
        TFWBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        TFWItems.ITEMS.register(modEventBus);
        TFWMenus.MENU_TYPES.register(modEventBus);
        GeneTypes.GENE_TYPES.register(modEventBus);
        TFWAbilitySerializers.ABILITIES.register(modEventBus);
        TFWDataComponents.DATA_COMPONENTS.register(modEventBus);

    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(GeneType.GENE_TYPE_REGISTRY);
    }
}
