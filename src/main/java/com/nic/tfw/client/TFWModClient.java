package com.nic.tfw.client;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.menu.TFWMenus;
import com.nic.tfw.client.gui.screen.MicroscopeScreen;
import com.nic.tfw.client.render.item.DNATintSource;
import com.nic.tfw.client.render.item.VialStateModelProperty;
import com.nic.tfw.client.render.item.LiquidAmountModelProperty;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@Mod(value = TFWMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TFWMod.MOD_ID, value = Dist.CLIENT)
public class TFWModClient {

    public TFWModClient(ModContainer container){}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event){
        event.register(TFWMenus.MICROSCOPE_MENU.get(), MicroscopeScreen::new);
    }

    @SubscribeEvent
    public static void registerModelProperties(RegisterSelectItemModelPropertyEvent event){
        event.register(
                Identifier.fromNamespaceAndPath(TFWMod.MOD_ID,
                        "vial_state"),
                VialStateModelProperty.TYPE
        );
    }

    @SubscribeEvent
    public static void registerModelProperties(RegisterRangeSelectItemModelPropertyEvent event){
        event.register(
                Identifier.fromNamespaceAndPath(TFWMod.MOD_ID,
                        "liquid_amount"),
                LiquidAmountModelProperty.CODEC
        );
    }

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event){
       event.register(Identifier.fromNamespaceAndPath(TFWMod.MOD_ID, "dna_layer"), DNATintSource.MAP_CODEC);
    }
}
