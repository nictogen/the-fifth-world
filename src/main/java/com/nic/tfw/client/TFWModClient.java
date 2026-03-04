package com.nic.tfw.client;

import com.nic.tfw.TFWMod;
import com.nic.tfw.block.menu.TFWMenus;
import com.nic.tfw.client.gui.screen.MicroscopeScreen;
import com.nic.tfw.client.render.item.GeneticDataModelProperty;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@Mod(value = TFWMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TFWMod.MODID, value = Dist.CLIENT)
public class TFWModClient {

    public TFWModClient(ModContainer container){}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event){
        event.register(TFWMenus.MICROSCOPE_MENU.get(), MicroscopeScreen::new);
    }

    @SubscribeEvent
    public static void registerModelProperties(RegisterSelectItemModelPropertyEvent event){
        event.register(
                Identifier.fromNamespaceAndPath(TFWMod.MODID,
                        "genetic_state"),
                GeneticDataModelProperty.TYPE
        );
    }
}
