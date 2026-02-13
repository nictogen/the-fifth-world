package com.nic.tfw.genetics.genetypes;

import com.nic.tfw.genetics.GeneticData;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeGeneType extends GeneType<Double> {

    public final Holder<Attribute> attribute;

    public AttributeGeneType(Holder<Attribute> attribute) {
        this.attribute = attribute;
    }

    @Override
    void apply(GeneticData<Double> data, LivingEntity entity) {
        entity.getAttribute(attribute).setBaseValue(data.value());
    }

    @Override
    public Double createGeneticValue(LivingEntity entity) {
        return entity.getAttributeBaseValue(this.attribute);
    }
}
