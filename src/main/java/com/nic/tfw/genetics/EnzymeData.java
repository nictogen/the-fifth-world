package com.nic.tfw.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.awt.*;

public record EnzymeData(Color color1, Color color2) {

    public static final Codec<EnzymeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            ExtraCodecs.RGB_COLOR_CODEC.xmap(Color::new, Color::getRGB).fieldOf("color_1").forGetter(enzymeData -> enzymeData.color1),
            ExtraCodecs.RGB_COLOR_CODEC.xmap(Color::new, Color::getRGB).fieldOf("color_2").forGetter(enzymeData -> enzymeData.color2)
            ).apply(i, EnzymeData::new));


}
