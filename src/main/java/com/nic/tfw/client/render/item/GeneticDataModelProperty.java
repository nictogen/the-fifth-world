package com.nic.tfw.client.render.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.nic.tfw.items.GeneState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record GeneticDataModelProperty() implements SelectItemModelProperty<GeneState> {

    public static final SelectItemModelProperty.Type<GeneticDataModelProperty, GeneState> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new GeneticDataModelProperty()),
            GeneState.CODEC
    );

    @Override
    public @Nullable GeneState get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return GeneState.getState(itemStack);
    }

    @Override
    public Codec<GeneState> valueCodec() {
        return GeneState.CODEC;
    }

    @Override
    public SelectItemModelProperty.Type<GeneticDataModelProperty, GeneState> type() {
        return TYPE;
    }

}
