package com.nic.tfw.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nic.tfw.genetics.genetypes.GeneType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.awt.*;

public record GeneticData(GeneType geneType, CompoundTag value, Color color1, Color color2, GeneticOrigin origin) {

    public static final Codec<GeneticData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                                    GeneType.GENE_TYPE_REGISTRY
                                            .byNameCodec()
                                            .fieldOf("gene_type")
                                            .forGetter(geneticData -> geneticData.geneType),
                                    CompoundTag.CODEC.fieldOf("value")
                                            .forGetter(geneticData -> geneticData.value),
                                    ExtraCodecs.RGB_COLOR_CODEC.xmap(Color::new, Color::getRGB).fieldOf("color_1").forGetter(geneticData -> geneticData.color1),
                                    ExtraCodecs.RGB_COLOR_CODEC.xmap(Color::new, Color::getRGB).fieldOf("color_2").forGetter(geneticData -> geneticData.color2),
                                    GeneticOrigin.CODEC.fieldOf("origin").forGetter(geneticData -> geneticData.origin))
                            .apply(instance, GeneticData::new)
            );

    public static final StreamCodec<FriendlyByteBuf, GeneticData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.idMapper(GeneType.GENE_TYPE_REGISTRY), GeneticData::geneType,
                    ByteBufCodecs.COMPOUND_TAG, GeneticData::value,
                    ByteBufCodecs.RGB_COLOR, geneticData -> geneticData.color1.getRGB(),
                    ByteBufCodecs.RGB_COLOR, geneticData -> geneticData.color2.getRGB(),
                    GeneticOrigin.STREAM_CODEC, GeneticData::origin,
                    (geneType, compoundTag, integer1, integer2, geneticOrigin) -> new GeneticData(geneType, compoundTag, new Color(integer1), new Color(integer2), geneticOrigin));
}
