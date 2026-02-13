package com.nic.tfw.genetics.genetypes;

import com.nic.tfw.TFWMod;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GeneTypes
{
    public static final DeferredRegister<GeneType<?>> GENE_TYPES = DeferredRegister.create(GeneType.GENE_TYPE_REGISTRY, TFWMod.MODID);

    public static final DeferredHolder<GeneType<?>, AttributeGeneType> STRENGTH = GENE_TYPES.register("strength", () -> new AttributeGeneType(Attributes.ATTACK_DAMAGE));
}
