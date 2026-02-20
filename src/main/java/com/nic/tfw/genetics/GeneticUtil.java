package com.nic.tfw.genetics;

import com.nic.tfw.genetics.genetypes.GeneType;
import com.nic.tfw.genetics.genetypes.GeneTypes;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneticUtil {

    public static List<GeneticData> createGeneticData(LivingEntity entity) {
        var data = new ArrayList<GeneticData>();
        createGeneticData(GeneTypes.STRENGTH.get(), entity).ifPresent(data::add);
        return data;
    }

    private static Optional<GeneticData> createGeneticData(GeneType geneType, LivingEntity entity) {
        return geneType.createGeneticValue(entity).map(compoundTag -> new GeneticData(geneType, compoundTag, Color.BLACK, new GeneticOrigin(entity.getType().toString(), entity.getUUID())));
    }
}
