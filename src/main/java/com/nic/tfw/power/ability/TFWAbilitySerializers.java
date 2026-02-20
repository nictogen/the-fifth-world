package com.nic.tfw.power.ability;

import com.nic.tfw.TFWMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class TFWAbilitySerializers {

    public static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create(PalladiumRegistryKeys.ABILITY_SERIALIZER, TFWMod.MODID);

}
