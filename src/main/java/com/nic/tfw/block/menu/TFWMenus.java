package com.nic.tfw.block.menu;

import com.nic.tfw.TFWMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TFWMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, TFWMod.MOD_ID);

    public static final Supplier<MenuType<MicroscopeMenu>> MICROSCOPE_MENU = MENU_TYPES.register(
            "microscope_menu",
            () -> new MenuType<>((IContainerFactory<MicroscopeMenu>) MicroscopeMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
}
