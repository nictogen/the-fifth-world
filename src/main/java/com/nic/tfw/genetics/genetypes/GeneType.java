package com.nic.tfw.genetics.genetypes;

import com.nic.tfw.TFWMod;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Optional;

public abstract class GeneType {
    public static final ResourceKey<Registry<GeneType>> GENE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(TFWMod.MOD_ID, "gene_types"));
    public static final Registry<GeneType> GENE_TYPE_REGISTRY = new RegistryBuilder<>(GENE_TYPE_REGISTRY_KEY).create();

    public abstract Optional<CompoundTag> createGeneticValue(LivingEntity entity);
    public abstract Component createValueComponent(CompoundTag tag);
}
