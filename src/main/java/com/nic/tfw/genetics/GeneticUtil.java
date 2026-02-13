package com.nic.tfw.genetics;

import com.nic.tfw.genetics.genetypes.AttributeGeneType;
import com.nic.tfw.genetics.genetypes.GeneType;
import com.nic.tfw.genetics.genetypes.GeneTypes;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneticUtil {

    public static List<GeneticData<?>> createGeneticData(LivingEntity entity){
        var data = new ArrayList<GeneticData<?>>();
        createAttributeData(GeneTypes.STRENGTH.get(), entity).ifPresent(data::add);
        return data;
    }

    private static Optional<GeneticData<Double>> createAttributeData(AttributeGeneType geneType, LivingEntity entity){
        if(entity.getAttributes().hasAttribute(geneType.attribute))
            return Optional.of(createGeneticData(geneType, entity));
        return Optional.empty();
    }

    private static <T> GeneticData<T> createGeneticData(GeneType<T> geneType, LivingEntity entity) {
        return new GeneticData<>(geneType, geneType.createGeneticValue(entity), Color.BLACK, new GeneticOrigin(entity.getType().toString(), entity.getUUID()));
    }
}
