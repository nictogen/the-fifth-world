package com.nic.tfw.client.render.item;

import com.mojang.serialization.MapCodec;
import com.nic.tfw.component.TFWDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record LiquidAmountModelProperty() implements RangeSelectItemModelProperty {

    public static final MapCodec<LiquidAmountModelProperty> CODEC = MapCodec.unit(new LiquidAmountModelProperty());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable ItemOwner itemOwner, int i) {
        return itemStack.getOrDefault(TFWDataComponents.LIQUID_AMOUNT, 0);
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return CODEC;
    }


}
