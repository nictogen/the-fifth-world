package com.nic.tfw.genetics;

import com.nic.tfw.TFWMod;
import com.nic.tfw.genetics.genetypes.GeneType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneticUtil {

    public static List<GeneticData> createGeneticData(LivingEntity entity) {
        var data = new ArrayList<GeneticData>();
        GeneType.GENE_TYPE_REGISTRY.stream().forEach(geneType -> createGeneticData(geneType, entity).ifPresent(data::add));
        return data;
    }

    private static Optional<GeneticData> createGeneticData(GeneType geneType, LivingEntity entity) {
        var id = GeneType.GENE_TYPE_REGISTRY.getKey(geneType);
        var colors = getColors(entity.getUUID().getMostSignificantBits() ^ entity.getUUID().getLeastSignificantBits() ^ id.hashCode());
        return geneType.createGeneticValue(entity).map(compoundTag -> new GeneticData(
                geneType,
                compoundTag,
                colors[0],
                colors[1],
                new GeneticOrigin(entity.getType().toString(), entity.getUUID())));
    }

    private static Color[] getColors(long seed) {
        var colors = new Color[] {
                new Color(151, 22, 7), new Color(180, 104, 77), new Color(222, 177, 45),
                new Color(17, 160, 54), new Color(33, 73, 123), new Color(154, 92, 198)
        };
        int i = Math.floorMod(seed, colors.length);
        int i2 = Math.floorMod(seed + 1, colors.length);
        return new Color[] {colors[i], colors[i2]};
    }

    public static Component getTitle(GeneType type){
        var key = GeneType.GENE_TYPE_REGISTRY.getKey(type);
        if(key == null) key = Identifier.fromNamespaceAndPath(TFWMod.MODID, "error");
        return Component.translatable("gene." + key.getNamespace() + "." + key.getPath() + ".name");
    }

    public static Component getDesc(GeneType type){
        var key = GeneType.GENE_TYPE_REGISTRY.getKey(type);
        if(key == null) key = Identifier.fromNamespaceAndPath(TFWMod.MODID, "error");
        return Component.translatable("gene." + key.getNamespace() + "." + key.getPath() + ".desc");
    }
}
