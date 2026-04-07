package com.nic.tfw.client.render.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.nic.tfw.items.VialState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record VialStateModelProperty() implements SelectItemModelProperty<VialState> {

    public static final SelectItemModelProperty.Type<VialStateModelProperty, VialState> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new VialStateModelProperty()),
            VialState.CODEC
    );

    @Override
    public @Nullable VialState get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return VialState.getState(itemStack);
    }

    @Override
    public Codec<VialState> valueCodec() {
        return VialState.CODEC;
    }

    @Override
    public SelectItemModelProperty.Type<VialStateModelProperty, VialState> type() {
        return TYPE;
    }

}
