package com.nic.tfw;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nic.tfw.genetics.GeneticUtil;
import com.nic.tfw.power.ability.TFWAbilitySerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.*;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class DisplayDNAAbility extends Ability {

    public static final MapCodec<DisplayDNAAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()).apply(instance, DisplayDNAAbility::new));

    public DisplayDNAAbility(AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if(enabled) {
            System.out.println(GeneticUtil.createGeneticData(entity));
        }
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return TFWAbilitySerializers.DISPLAY_DNA.get();
    }

    public static class Serializer extends AbilitySerializer<DisplayDNAAbility> {

        @Override
        public MapCodec<DisplayDNAAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, DisplayDNAAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Display's the entity's DNA")
                    .addExampleObject(new DisplayDNAAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
