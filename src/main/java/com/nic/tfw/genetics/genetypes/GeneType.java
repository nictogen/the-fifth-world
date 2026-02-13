package com.nic.tfw.genetics.genetypes;

import com.nic.tfw.TFWMod;
import com.nic.tfw.genetics.GeneticData;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.RegistryBuilder;

public abstract class GeneType<T> {
    public static final ResourceKey<Registry<GeneType<?>>> GENE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(TFWMod.MODID, "gene_types"));
    public static final Registry<GeneType<?>> GENE_TYPE_REGISTRY = new RegistryBuilder<>(GENE_TYPE_REGISTRY_KEY).create();

    //TODO remember to try/catch, increasing mutation if it throws an exception instead of applying
    abstract void apply(GeneticData<T> data, LivingEntity entity);
    public abstract T createGeneticValue(LivingEntity entity);
}
