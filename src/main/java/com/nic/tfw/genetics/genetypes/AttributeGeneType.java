package com.nic.tfw.genetics.genetypes;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Optional;

public class AttributeGeneType extends GeneType {

    public final Holder<Attribute> attribute;

    public AttributeGeneType(Holder<Attribute> attribute) {
        this.attribute = attribute;
    }

    @Override
    public Optional<CompoundTag> createGeneticValue(LivingEntity entity) {
        if(entity.getAttributes().hasAttribute(attribute)){
            var tag = new CompoundTag();
            tag.putString(attribute.getRegisteredName(), String.valueOf(entity.getAttributeBaseValue(attribute)));
            return Optional.of(tag);
        }
        return Optional.empty();
    }

}
