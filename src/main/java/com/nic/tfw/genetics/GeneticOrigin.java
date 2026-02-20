package com.nic.tfw.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record GeneticOrigin(String species, UUID donorUUID) {
    public static final MapCodec<GeneticOrigin> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("species").forGetter(geneticOrigin -> geneticOrigin.species),
            Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("donor_uuid").forGetter(geneticOrigin -> geneticOrigin.donorUUID)
    ).apply(i, GeneticOrigin::new));


    public static final StreamCodec<ByteBuf, GeneticOrigin> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, GeneticOrigin::species,
                    UUIDUtil.STREAM_CODEC, GeneticOrigin::donorUUID,
                    GeneticOrigin::new);
}
