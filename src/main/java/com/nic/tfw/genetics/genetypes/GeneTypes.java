package com.nic.tfw.genetics.genetypes;

import com.nic.tfw.TFWMod;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GeneTypes
{
    public static final DeferredRegister<GeneType> GENE_TYPES = DeferredRegister.create(GeneType.GENE_TYPE_REGISTRY, TFWMod.MOD_ID);

    public static final DeferredHolder<GeneType, AttributeGeneType> STRENGTH = GENE_TYPES.register("strength", () -> new AttributeGeneType(Attributes.ATTACK_DAMAGE));
    public static final DeferredHolder<GeneType, AttributeGeneType> JUMP_STRENGTH = GENE_TYPES.register("jump_strength", () -> new AttributeGeneType(Attributes.JUMP_STRENGTH));
    public static final DeferredHolder<GeneType, AttributeGeneType> HEALTH = GENE_TYPES.register("health", () -> new AttributeGeneType(Attributes.MAX_HEALTH));
    public static final DeferredHolder<GeneType, AttributeGeneType> SPEED = GENE_TYPES.register("speed", () -> new AttributeGeneType(Attributes.MOVEMENT_SPEED));
    public static final DeferredHolder<GeneType, AttributeGeneType> KNOCKBACK_RESISTANCE = GENE_TYPES.register("knockback_resistance", () -> new AttributeGeneType(Attributes.KNOCKBACK_RESISTANCE));
    public static final DeferredHolder<GeneType, AttributeGeneType> FALL_RESISTANCE = GENE_TYPES.register("fall_resistance", () -> new AttributeGeneType(Attributes.SAFE_FALL_DISTANCE));
    public static final DeferredHolder<GeneType, AttributeGeneType> ZOMBIE_SPAWN_CHANCE = GENE_TYPES.register("zombie_spawn_chance", () -> new AttributeGeneType(Attributes.SPAWN_REINFORCEMENTS_CHANCE));
    public static final DeferredHolder<GeneType, AttributeGeneType> STEP_HEIGHT = GENE_TYPES.register("step_height", () -> new AttributeGeneType(Attributes.STEP_HEIGHT));



}
