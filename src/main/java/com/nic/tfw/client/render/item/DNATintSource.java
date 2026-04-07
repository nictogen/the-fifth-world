package com.nic.tfw.client.render.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.nic.tfw.genetics.GeneticUtil;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record DNATintSource(int layer) implements ItemTintSource {

    public static final MapCodec<DNATintSource> MAP_CODEC = Codec.INT.fieldOf("layer").xmap(DNATintSource::new, DNATintSource::layer);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return GeneticUtil.getColor(itemStack, layer).getRGB();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
