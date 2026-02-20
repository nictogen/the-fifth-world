package com.nic.tfw.genetics;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nic.tfw.genetics.genetypes.GeneType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.awt.*;

public record GeneticData(GeneType geneType, CompoundTag value, Color color, GeneticOrigin origin) {

    public static final MapCodec<GeneticData> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            GeneType.GENE_TYPE_REGISTRY
                                    .byNameCodec()
                                    .fieldOf("gene_type")
                                    .forGetter(geneticData -> geneticData.geneType),
                            CompoundTag.CODEC.fieldOf("value")
                                    .forGetter(geneticData -> geneticData.value),
                            ExtraCodecs.RGB_COLOR_CODEC.xmap(Color::new, Color::getRGB).fieldOf("color").forGetter(geneticData -> geneticData.color),
                            GeneticOrigin.CODEC.fieldOf("origin").forGetter(geneticData -> geneticData.origin))
                            .apply(instance, GeneticData::new)
            );

    public static final StreamCodec<FriendlyByteBuf, GeneticData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.idMapper(GeneType.GENE_TYPE_REGISTRY), GeneticData::geneType,
                    ByteBufCodecs.COMPOUND_TAG, GeneticData::value,
                    ByteBufCodecs.RGB_COLOR, geneticData -> geneticData.color.getRGB(),
                    GeneticOrigin.STREAM_CODEC, GeneticData::origin,
                    (geneType, compoundTag, integer, geneticOrigin) -> new GeneticData(geneType, compoundTag, new Color(integer), geneticOrigin));
}
