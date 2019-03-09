package com.nic.tfw.superpower.genes;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.he.HEGenes;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import com.nic.tfw.superpower.abilities.*;
import com.nic.tfw.superpower.abilities.defects.*;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.karma.KarmaStat;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nictogen on 1/18/19.
 */
@Mod.EventBusSubscriber
public class GeneHandler
{
	public static IForgeRegistry<Gene> GENE_REGISTRY;

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event)
	{
		GENE_REGISTRY = new RegistryBuilder<Gene>().setName(new ResourceLocation(LucraftCore.MODID, "genes")).setType(Gene.class).setIDRange(0, 512).create();
	}

	public static final UUID RANDOM_UUID = UUID.fromString("dc7bfdde-5e0e-44b8-ae43-20063182cd8b");

	@SuppressWarnings("WeakerAccess")
	public static class LucraftCoreGenes
	{
		//Attribute Modifier
		public static final Gene health = new Gene(AbilityHealth.class, "Health Boost").addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 5f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene strength = new Gene(AbilityStrength.class, "Strength Boost")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 5f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene punch = new Gene(AbilityPunch.class, "Punch Boost").addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 5f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene sprint = new Gene(AbilitySprint.class, "Sprint Boost").addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 0.3f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene jump_boost = new Gene(AbilityJumpBoost.class, "Jump Boost")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 0.3f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene resistance = new Gene(AbilityDamageResistance.class, "Resistance")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 35f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene fall_resistance = new Gene(AbilityFallResistance.class, "Fall Resistance")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 8f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene knockback_resistance = new Gene(AbilityKnockbackResistance.class, "Knockback Resistance")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 8f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));
		public static final Gene step_assist = new Gene(AbilityStepAssist.class, "Step Assist")
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.AMOUNT, 2f))
				.addDataMod(new Gene.DataMod<>(AbilityAttributeModifier.UUID, RANDOM_UUID, false));

		//Potion Punch
		public static final Gene potion_punch_minecraft_poison = new GenePotionPunch("Poison Punch", 19, 500, 3);
		public static final Gene potion_punch_minecraft_slowness = new GenePotionPunch("Slowness Punch", 2, 500, 5);
		public static final Gene potion_punch_minecraft_weakness = new GenePotionPunch("Weakness Punch", 18, 500, 2);
		public static final Gene potion_punch_minecraft_nausea = new GenePotionPunch("Nausea Punch", 9, 250, 1);
		public static final Gene potion_punch_minecraft_mining_fatigue = new GenePotionPunch("Fatigue Punch", 4, 500, 1);

		public static final Gene potion_punch_minecraft_instant_health = new GenePotionPunch("Healing Punch", 6, 1, 10);
		public static final Gene potion_punch_minecraft_blindness = new GenePotionPunch("Blinding Punch", 15, 250, 1);
		public static final Gene potion_punch_minecraft_hunger = new GenePotionPunch("Hunger Punch", 17, 500, 10);
		public static final Gene potion_punch_minecraft_wither = new GenePotionPunch("Wither Punch", 20, 400, 3);
		public static final Gene potion_punch_minecraft_levitation = new GenePotionPunch("Levitation Punch", 25, 250, 10);

		//Misc
		public static final Gene healing = new Gene(AbilityHealing.class, "Healing Factor").addDataMod(new Gene.DataMod<>(AbilityHealing.AMOUNT, 2.5f));
		public static final Gene fire_resistance = new Gene(AbilityFireResistance.class, "Fire Resistance");
		//		public static final Gene size_change = null;
		public static final Gene teleport = new Gene(AbilityTeleport.class, "Teleportation").addDataMod(new Gene.DataMod<>(AbilityTeleport.DISTANCE, 50f))
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 600, true, true));
		public static final Gene slowfall = new Gene(AbilitySlowfall.class, "Slow Falling");
		public static final Gene energy_blast = new GeneEnergyBlast("Energy Blast", 8f);
		public static final Gene fire_punch = new Gene(AbilityFirePunch.class, "Fire Punch").addDataMod(new Gene.DataMod<>(AbilityFirePunch.DURATION, 50));
		public static final Gene flight = new Gene(AbilityFlight.class, "Flight").addDataMod(new Gene.DataMod<>(AbilityFlight.SPEED, 0.4f))
				.addDataMod(new Gene.DataMod<>(AbilityFlight.SPRINT_SPEED, 2f));
		public static final Gene water_breathing = new Gene(AbilityWaterBreathing.class, "Water Breathing");
		public static final Gene tough_lungs = new Gene(AbilityToughLungs.class, "Tough Lungs");
		public static final Gene invisibility = new Gene(AbilityInvisibility.class, "Invisibility");
	}

	@SuppressWarnings("WeakerAccess") @GameRegistry.ObjectHolder(TheFifthWorld.MODID)
	public static class FifthWorldGenes
	{
		public static final Gene make_hostile = new Gene(AbilityMakeHostile.class, "Hostile");
		public static final Gene mountable = new Gene(AbilityMountable.class, "Mountable");
		public static final Gene screech = new Gene(AbilityScreech.class, "Screech").addDataMod(new Gene.DataMod<>(AbilityScreech.DAMAGE, 5f));
		public static final Gene eat_hay = new GeneEatBlock("Eat Hay", Blocks.HAY_BLOCK, Blocks.AIR, 16, 2f).setRegistryName(TheFifthWorld.MODID, "eat_hay");
		public static final Gene graze = new GeneEatBlock("Graze", Blocks.GRASS, Blocks.DIRT, 8, 1f).setRegistryName(TheFifthWorld.MODID, "graze");
		public static final Gene lay_egg = new Gene(AbilityLayEgg.class, "Lay Egg").addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 600, true, true));
		public static final Gene explode = new Gene(AbilityExplode.class, "Explode").addDataMod(new Gene.DataMod<>(AbilityExplode.STRENGTH, 5f))
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 1000, true, true));
		public static final Gene give_potion_minecraft_nightvision = new GeneGivePotion("Give Nightvision", Potion.getIdFromPotion(MobEffects.NIGHT_VISION), 1);
		public static final Gene bonemeal = new Gene(AbilityBonemealArea.class, "Bonemeal Area")
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 1000, true, true)).addDataMod(new Gene.DataMod<>(AbilityBonemealArea.RADIUS, 10));

		//Potion Immunity
		public static final Gene potion_immunity_minecraft_poison = new GenePotionImmunity("Poison Immunity", 19);
		public static final Gene potion_immunity_minecraft_slowness = new GenePotionImmunity("Slowness Immunity", 2);
		public static final Gene potion_immunity_minecraft_weakness = new GenePotionImmunity("Weakness Immunity", 18);
		public static final Gene potion_immunity_minecraft_nausea = new GenePotionImmunity("Nausea Immunity", 9);
		public static final Gene potion_immunity_minecraft_mining_fatigue = new GenePotionImmunity("Fatigue Immunity", 4);
		public static final Gene potion_immunity_minecraft_blindness = new GenePotionImmunity("Blindness Immunity", 15);
		public static final Gene potion_immunity_minecraft_hunger = new GenePotionImmunity("Hunger Immunity", 17);
		public static final Gene potion_immunity_minecraft_wither = new GenePotionImmunity("Wither Immunity", 20);
		public static final Gene potion_immunity_minecraft_levitation = new GenePotionImmunity("Levitation Immunity", 25);

		//Item Creation
		public static final Gene create_item_minecraft_slime_ball = new GeneItemCreation("Create Slime", 1000, new ItemStack(Items.SLIME_BALL));
		public static final Gene create_item_minecraft_golden_sword = new GeneItemCreation("Create Golden Sword", 1000, new ItemStack(Items.GOLDEN_SWORD));
		public static final Gene create_item_minecraft_gold_nugget = new GeneItemCreation("Create Gold Nugget", 700, new ItemStack(Items.GOLD_NUGGET));
		public static final Gene create_item_minecraft_ghast_tear = new GeneItemCreation("Create Ghast Tear", 1000, new ItemStack(Items.GHAST_TEAR));
		public static final Gene create_item_minecraft_ender_pearl = new GeneItemCreation("Create Ender Pearl", 500, new ItemStack(Items.ENDER_PEARL));
		public static final Gene create_item_minecraft_snowball = new GeneItemCreation("Create Snowballs", 300, new ItemStack(Items.SNOWBALL));
		public static final Gene create_item_minecraft_arrow = new GeneItemCreation("Create Arrow", 500, new ItemStack(Items.ARROW));

		//Item Change
		public static final Gene milk_entity = new Gene(AbilityChangeItem.class, "Milk Extraction", true).setRegistryName(TheFifthWorld.MODID, "milk")
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 600, true, true))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.FROM, new ItemStack(Items.BUCKET), false))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.TO, new ItemStack(Items.MILK_BUCKET), false));
		public static final Gene soup_entity = new Gene(AbilityChangeItem.class, "Soup Extraction", true).setRegistryName(TheFifthWorld.MODID, "soup")
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 600, true, true))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.FROM, new ItemStack(Items.BOWL), false))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.TO, new ItemStack(Items.MUSHROOM_STEW), false));
		public static final Gene lava_entity = new Gene(AbilityChangeItem.class, "Lava Extraction", true).setRegistryName(TheFifthWorld.MODID, "lava")
				.addDataMod(new Gene.DataMod<>(Ability.MAX_COOLDOWN, 1000, true, true))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.FROM, new ItemStack(Items.BUCKET), false))
				.addDataMod(new Gene.DataMod<>(AbilityChangeItem.TO, new ItemStack(Items.LAVA_BUCKET), false));

		//Entity Summoning
		public static final Gene summon_zombie = new GeneSummonEntity("Summon Zombie", "minecraft:zombie", 5, 2000);
		public static final Gene summon_bats = new GeneSummonEntity("Summon Bats", "minecraft:bat", 15, 2000);

	}

	public static void populateGeneList()
	{
		for (Field field : LucraftCoreGenes.class.getFields())
			try
			{
				GENE_REGISTRY.register((Gene) field.get(null));
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

		for (Field field : FifthWorldGenes.class.getFields())
			try
			{
				GENE_REGISTRY.register((Gene) field.get(null));
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

		if (Loader.isModLoaded("heroesexpansion"))
			HEGenes.populateGeneList();

		//Defects
		GENE_REGISTRY.register(new GeneDefect(InvoluntaryExplosion.class, "Involuntarily Explodes").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(ButterFingers.class, "Butter Fingers").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(SetOnFire.class, "Spontaneous Combustion").setAlwaysOnChance(0.2f));
		GENE_REGISTRY.register(new GeneDefect(RestrictiveDiet.class, "Can't Eat").setAlwaysOnChance(0.9f));
		GENE_REGISTRY.register(new GeneDefect(Deafness.class, "Deaf"));
		GENE_REGISTRY.register(new GeneDefect(LightningRod.class, "Lightning Rod"));
		GENE_REGISTRY.register(new GeneDefect(Starving.class, "Starving"));
		//		GENE_REGISTRY.register(new GeneDefect(DefectEnable.class, "Enabling Power").setAlwaysOnChance(0.0f));
		//		GENE_REGISTRY.register(new GeneDefect(DefectDisable.class, "Disabling Power").setAlwaysOnChance(0.0f));

	}

	public static void addSpeciesGenes(EntityLivingBase entity, GeneSet set)
	{

		Random r = new Random(entity.getPersistentID().getMostSignificantBits() + entity.getPersistentID().getLeastSignificantBits());

		/*
		 * Passive Mobs
		 */
		if (entity instanceof EntityBat)
		{
			set.addGene(entity, LucraftCoreGenes.flight, GeneStrength.LOW.chance, r);
			set.addGene(entity, FifthWorldGenes.screech, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.give_potion_minecraft_nightvision, 1.0f);
			set.addGene(entity, FifthWorldGenes.summon_bats, GeneStrength.HIGH.chance, r);
			set.addGene(entity, HEGenes.wall_crawling, 1.0f);
			//			Drain blood
			//			Bite
		}
		if (entity instanceof EntityChicken)
		{
			set.addGene(entity, LucraftCoreGenes.flight, GeneStrength.VERY_LOW.chance, r);
			set.addGene(entity, LucraftCoreGenes.slowfall, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.fall_resistance, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.lay_egg, GeneStrength.PERFECT.chance, r);
		}
		if (entity instanceof EntityCow)
		{
			set.addGene(entity, LucraftCoreGenes.health, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.healing, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.knockback_resistance, GeneStrength.MID.chance, r);
			set.addGene(entity, FifthWorldGenes.milk_entity, GeneStrength.HIGH.chance, r);
			//			Charge
		}
		if (entity instanceof EntityMooshroom)
		{
			set.addGene(entity, FifthWorldGenes.soup_entity, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.bonemeal, GeneStrength.HIGH.chance, r);
			//Shoot vines up until top of wall
			//Psychotropic effect
			//Grass to mycelium
		}
		if (entity instanceof EntityDonkey)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.jump_boost, GeneStrength.MID.chance, r);
			set.addGene(entity, FifthWorldGenes.eat_hay, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			//Bite
		}
		else if (entity instanceof EntityMule)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.jump_boost, GeneStrength.LOW.chance, r);
			set.addGene(entity, FifthWorldGenes.eat_hay, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			//Bite
		}
		else if (entity instanceof EntityHorse)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.LOW.chance, r);
			set.addGene(entity, LucraftCoreGenes.jump_boost, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.sprint, GeneStrength.MID.chance, r);
			set.addGene(entity, FifthWorldGenes.eat_hay, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			//Bite
		}
		if (entity instanceof EntityOcelot)
		{
			set.addGene(entity, LucraftCoreGenes.sprint, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, LucraftCoreGenes.fall_resistance, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_slowness, 1.0f);
			//Predator Vision?
			//Bite
			//Stealth
			//Pack Leader
		}
		if (entity instanceof EntityParrot)
		{
			set.addGene(entity, LucraftCoreGenes.flight, GeneStrength.HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.slowfall, GeneStrength.HIGH.chance, r);
			//Power Mimicry
			//Send messages as other player
		}
		if (entity instanceof EntityPig)
		{
			set.addGene(entity, LucraftCoreGenes.health, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.healing, GeneStrength.MID.chance, r);
			set.addGene(entity, LucraftCoreGenes.knockback_resistance, GeneStrength.MID.chance, r);
			//Eat anything?
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
		}
		if (entity instanceof EntityRabbit)
		{
			set.addGene(entity, LucraftCoreGenes.jump_boost, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, LucraftCoreGenes.sprint, GeneStrength.MID.chance, r);
			//Burrowing
			//Luck
			//Hearing
		}
		if (entity instanceof EntitySheep)
		{
			set.addGene(entity, LucraftCoreGenes.health, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.graze, GeneStrength.PERFECT.chance, r);
			//Shearable
			//Herding
		}
		if (entity instanceof EntitySquid)
		{
			//Ink Attack
			//Ink Sac spawning
			//Water attack?
		}
		if (entity instanceof EntityVillager)
		{
			//Looting/Fortune for free
			//Smartness, eventually
			if (r.nextFloat() < 0.1f)
				set.addGene(entity, HEGenes.blindness, 1.0f);
		}

		/*
		 * Neutral Mobs
		 */

		if (entity instanceof EntityLlama)
		{
			set.addGene(entity, LucraftCoreGenes.step_assist, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			//Spitting
		}
		if (entity instanceof EntityPolarBear)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.VERY_HIGH.chance, r);
			//Fish in water
			//Bite
		}
		if (entity instanceof EntityWolf)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.PERFECT.chance, r);
			//			Bite
			//			Pack Leader?
			//			Predator Vision?
			//			Loyalty?
		}

		/*
		 * Hostile Mobs
		 */
		if (entity instanceof EntityCaveSpider)
		{
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_poison, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_poison, 1.0f);
			set.addGene(entity, HEGenes.web_wings, 1.0f);
			set.addGene(entity, HEGenes.spider_sense, 1.0f);
			//Bite
		}
		else if (entity instanceof EntitySpider)
		{
			//Web Attack
			//Bite
			set.addGene(entity, LucraftCoreGenes.invisibility, 1.0f);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_poison, 1.0f);
			set.addGene(entity, HEGenes.wall_crawling, 1.0f);
			set.addGene(entity, HEGenes.spider_sense, 1.0f);

		}
		if (entity instanceof EntityEnderman)
		{
			set.addGene(entity, LucraftCoreGenes.teleport, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_ender_pearl, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_nausea, 1.0f);
			//			Watcher detection
			//			Dimension jump
		}
		if (entity instanceof EntityPigZombie)
		{
			//			set.addGene(entity, LucraftCoreGenes.punch, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_golden_sword, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_gold_nugget, GeneStrength.VERY_HIGH.chance, r);
			//Lightning attacks?
			//Summon zombie pigman
		}
		if (entity instanceof EntityBlaze)
		{
			set.addGene(entity, LucraftCoreGenes.fire_resistance, 1.0f);
			set.addGene(entity, LucraftCoreGenes.fire_punch, GeneStrength.LOW.chance, r);
			set.addGene(entity, LucraftCoreGenes.slowfall, GeneStrength.MID.chance, r);
			set.addGene(entity, HEGenes.heat_vision, GeneStrength.PERFECT.chance, r);
			// Fireball
			// Fire blasts
			// Smoke Form
			// Smoke Cloud
		}
		if (entity instanceof EntityCreeper)
		{
			set.addGene(entity, FifthWorldGenes.explode, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, HEGenes.energy_absorption, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, HEGenes.lightning_attack, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, HEGenes.lightning_strike, GeneStrength.PERFECT.chance, r);
			//Explosion Punch
			//Electricity absorbtion
		}
		if (entity instanceof EntityElderGuardian)
		{
			set.addGene(entity, LucraftCoreGenes.energy_blast, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, HEGenes.photon_blast, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_blindness, 1.0f);   //Visions
			set.addGene(entity, HEGenes.precision, 1.0f);
			//Long Range Potion effect
		}
		else if (entity instanceof EntityGuardian)
		{
			set.addGene(entity, LucraftCoreGenes.energy_blast, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, HEGenes.photon_blast, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_blindness, 1.0f);
			//Thorn projectiles
			//Predator Vision
			//Thorn when attacked
			//Lock on?
		}
		if (entity instanceof EntityEndermite)
		{
			set.addGene(entity, LucraftCoreGenes.teleport, GeneStrength.MID.chance, r);
		}
		if (entity instanceof EntityEvoker)
		{
			//weird behavior because of magic
			//eventual sw genes
		}
		if (entity instanceof EntityGhast)
		{
			set.addGene(entity, LucraftCoreGenes.fire_resistance, 1.0f);
			set.addGene(entity, LucraftCoreGenes.flight, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_ghast_tear, GeneStrength.VERY_HIGH.chance, r);
			// Fireball
			// Wail
			// Tentacles
			// Crying attack?
			// Eventual The Weeper powers?
		}
		if (entity instanceof EntityHusk)
		{
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_hunger, GeneStrength.PERFECT.chance, r);
		}
		if (entity instanceof EntityZombie)
		{
			set.addGene(entity, LucraftCoreGenes.healing, GeneStrength.MID.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_hunger, 1.0f);
			set.addGene(entity, FifthWorldGenes.summon_zombie, GeneStrength.HIGH.chance, r);
			//Infect with superpower
			//Bite
			//Turn villagers into zombies
			//Death touch?
			//Immortality
			//Kill surrounding plant life
			//Kind of want to beat someone with my own limb?
		}
		if (entity instanceof EntityMagmaCube)
		{
			set.addGene(entity, LucraftCoreGenes.fire_resistance, 1.0f);
			set.addGene(entity, FifthWorldGenes.lava_entity, GeneStrength.HIGH.chance, r);
			//Fire when hit
			//Lava creation/attacks?
		}
		if (entity instanceof EntitySlime)
		{
			set.addGene(entity, LucraftCoreGenes.jump_boost, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_slime_ball, GeneStrength.VERY_HIGH.chance, r);
			//Reverse Knockback
			//Splitting into multiple
			//Create ooze?
		}
		if (entity instanceof EntityShulker)
		{
			set.addGene(entity, LucraftCoreGenes.teleport, GeneStrength.HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_levitation, 1.0f);
			//Fire bullet
			//Become block?
			//Shield
		}
		if (entity instanceof EntitySilverfish)
		{
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_mining_fatigue, 1.0f);
			//Become block
			//Summon silverfish
			//Burrow
		}
		if (entity instanceof EntitySkeleton)
		{
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_weakness, 1.0f);
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_arrow, GeneStrength.HIGH.chance, r);
			set.addGene(entity, HEGenes.precision, 1.0f);
			//Remove need to eat?
			//Any since magic?
		}
		if (entity instanceof EntityStray)
		{
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_slowness, GeneStrength.PERFECT.chance, r);
			//Regular arrows -> slowness ones
		}
		if (entity instanceof EntityVex)
		{
			set.addGene(entity, LucraftCoreGenes.flight, GeneStrength.HIGH.chance, r);
			//Expiration date
			//again, magic, so maybe mess up
		}
		if (entity instanceof EntityVindicator)
		{

		}
		if (entity instanceof EntityWitch)
		{
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_slowness, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_hunger, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_poison, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_blindness, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_instant_health, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_levitation, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_mining_fatigue, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_nausea, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_weakness, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_wither, GeneStrength.VERY_HIGH.chance, r);

			//Throw random potion
			//Magic resistance
			//mess up?
		}
		if (entity instanceof EntityWitherSkeleton)
		{
			set.addGene(entity, LucraftCoreGenes.fire_resistance, 1.0f);
			set.addGene(entity, LucraftCoreGenes.potion_punch_minecraft_wither, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_wither, 1.0f);
			//Fiery arrows
		}

		/*
		 * Utility Mobs
		 */
		if (entity instanceof EntityIronGolem)
		{
			set.addGene(entity, LucraftCoreGenes.health, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, LucraftCoreGenes.resistance, GeneStrength.PERFECT.chance, r);
			//Colossus
			//Pick up and throw
			//Metal manipulation?
			//Fling into air
		}
		if (entity instanceof EntitySnowman)
		{
			set.addGene(entity, FifthWorldGenes.create_item_minecraft_snowball, GeneStrength.VERY_HIGH.chance, r);
			//Leave snow trail
			//Throw snowballs
			//Ice powers
		}

		/*
		 * Boss Mobs
		 */
		if (entity instanceof EntityDragon)
		{
			//Dragon Breath
		}
		if (entity instanceof EntityWither)
		{
			//Cannonball powers
			set.addGene(entity, HEGenes.god_mode, GeneStrength.PERFECT.chance, r);
		}

		//General
		if (entity instanceof EntityWaterMob)
		{
			set.addGene(entity, LucraftCoreGenes.water_breathing, 1.0f);
		}
		if (entity instanceof EntityMob)
		{
			set.addGene(entity, FifthWorldGenes.make_hostile, 1.0f);
		}
	}

	public enum GeneStrength
	{
		NONE(0.0f),
		VERY_LOW(0.1f),
		LOW(0.2f),
		MID(0.3f),
		HIGH(0.4f),
		VERY_HIGH(0.5f),
		PERFECT(0.6f);

		public float chance;

		GeneStrength(float chance)
		{
			this.chance = chance;
		}
	}

	static ItemStack createGeneDataBook(GeneSet geneSet, EntityLivingBase attacker)
	{
		ItemStack s = new ItemStack(Items.WRITTEN_BOOK);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("title", "Gene Data");
		nbt.setString("author", attacker.getDisplayName().getFormattedText());
		NBTTagList pages = new NBTTagList();

		int index = 0;

		StringBuilder s2 = new StringBuilder();

		index = addLine(s2, pages, "Genetic Abilities:", index);
		for (GeneSet.GeneData gene : geneSet.genes.get(0))
		{
			if (!(gene.gene instanceof GeneDefect))
			{
				index = addLine(s2, pages, "", index);
				index = addLine(s2, pages, gene.gene.displayName, index);
				if (gene.gene.isQualified())
					index = addLine(s2, pages, Math.round(gene.quality * 1000f) / 10.0 + "% Quality", index);
			}
		}
		index = addLine(s2, pages, "", index);
		index = addLine(s2, pages, "Genetic Defects", index);
		index = addLine(s2, pages, "", index);

		for (GeneSet.GeneData gene : geneSet.genes.get(0))
		{
			if (gene.gene instanceof GeneDefect)
			{
				index = addLine(s2, pages, gene.gene.displayName, index);
			}
		}

		pages.appendTag(new NBTTagString(s2.toString()));

		for (int i = 0; i < pages.tagCount(); ++i)
		{
			String s1 = pages.getStringTagAt(i);
			ITextComponent itextcomponent = new TextComponentString(s1);
			s1 = ITextComponent.Serializer.componentToJson(itextcomponent);
			pages.set(i, new NBTTagString(s1));
		}
		nbt.setTag("pages", pages);
		s.setTagCompound(nbt);
		return s;
	}

	private static int addLine(StringBuilder stringBuilder, NBTTagList pages, String content, int index)
	{
		//TODO handle adding more than 20 characters
		if (index == 14)
		{
			pages.appendTag(new NBTTagString(stringBuilder.toString()));
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append(content);
			stringBuilder.append("\n");
			return 1;
		}
		else
			stringBuilder.append(content);
		stringBuilder.append("\n");
		return index + 1;
	}

	public static KarmaStat TEST_SUBJECTS_EXPLODED;
	public static KarmaStat DEFECTS_GIVEN;

	@SubscribeEvent
	public static void onRegisterKarmaStats(RegistryEvent.Register<KarmaStat> e)
	{
		e.getRegistry().register(TEST_SUBJECTS_EXPLODED = new KarmaStat("test_subjects_exploded", -75).setRegistryName(TheFifthWorld.MODID, "test_subjects_exploded"));
		e.getRegistry().register(DEFECTS_GIVEN = new KarmaStat("defects_given", -5).setRegistryName(TheFifthWorld.MODID, "defects_given"));
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof EntityPlayerMP && SuperpowerHandler.getSuperpower((EntityPlayer) event.getEntity()) instanceof SuperpowerGeneticallyModified)
			SuperpowerHandler.removeSuperpower((EntityPlayer) event.getEntity());
	}
}
