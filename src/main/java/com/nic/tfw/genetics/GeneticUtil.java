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
        return geneType.createGeneticValue(entity).map(compoundTag -> new GeneticData(
                geneType,
                compoundTag,
                getColor(entity.getUUID().getMostSignificantBits() ^ entity.getUUID().getLeastSignificantBits() ^ id.hashCode()),
                new GeneticOrigin(entity.getType().toString(), entity.getUUID())));
    }

    private static Color getColor(long seed) {
        var colors = new Color[] {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, new Color(125, 0, 255)};
        int i = Math.floorMod(seed, colors.length);
        var c = colors[i];
        return c;
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
