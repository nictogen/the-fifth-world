package com.nic.tfw.superpower.genes;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import com.nic.tfw.superpower.abilities.*;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

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

	@GameRegistry.ObjectHolder(LucraftCore.MODID)
	public static class LucraftCoreGenes
	{
		public static final Gene health = null;
		public static final Gene healing = null;
		public static final Gene strength = null;
		public static final Gene punch = null;
		public static final Gene sprint = null;
		public static final Gene jump_boost = null;
		public static final Gene resistance = null;
		public static final Gene fall_resistance = null;
		public static final Gene fire_resistance = null;
		public static final Gene step_assist = null;
		public static final Gene size_change = null;
		public static final Gene teleport = null;
		public static final Gene knockback_resistance = null;
		public static final Gene slowfall = null;
		public static final Gene energy_blast = null;
		public static final Gene fire_punch = null;
		public static final Gene flight = null;
		public static final Gene water_breathing = null;
		public static final Gene tough_lungs = null;
		public static final Gene invisibility = null;

		//Potion Punch
		public static final Gene potion_punch_minecraft_poison = null;
		public static final Gene potion_punch_minecraft_slowness = null;
		public static final Gene potion_punch_minecraft_weakness = null;
		public static final Gene potion_punch_minecraft_nausea = null;
		public static final Gene potion_punch_minecraft_mining_fatigue = null;
		public static final Gene potion_punch_minecraft_instant_health = null;
		public static final Gene potion_punch_minecraft_blindness = null;
		public static final Gene potion_punch_minecraft_hunger = null;
		public static final Gene potion_punch_minecraft_wither = null;
		public static final Gene potion_punch_minecraft_levitation = null;
	}

	@GameRegistry.ObjectHolder(TheFifthWorld.MODID)
	public static class FifthWorldGenes
	{
		public static final Gene make_hostile = null;
		public static final Gene screech = null;
		public static final Gene eat_hay = null;
		public static final Gene graze = null;
		public static final Gene lay_egg = null;
		public static final Gene explode = null;
		public static final Gene mountable = null;

		//Potion Immunity
		public static final Gene potion_immunity_minecraft_poison = null;
		public static final Gene potion_immunity_minecraft_slowness = null;
		public static final Gene potion_immunity_minecraft_weakness = null;
		public static final Gene potion_immunity_minecraft_nausea = null;
		public static final Gene potion_immunity_minecraft_mining_fatigue = null;
		public static final Gene potion_immunity_minecraft_instant_health = null;
		public static final Gene potion_immunity_minecraft_blindness = null;
		public static final Gene potion_immunity_minecraft_hunger = null;
		public static final Gene potion_immunity_minecraft_wither = null;
		public static final Gene potion_immunity_minecraft_levitation = null;

		//Item Creation
		public static final Gene create_item_minecraft_slime_ball = null;
		public static final Gene create_item_minecraft_golden_sword = null;
		public static final Gene create_item_minecraft_gold_nugget = null;
		public static final Gene create_item_minecraft_ghast_tear = null;
		public static final Gene create_item_minecraft_ender_pearl = null;
		public static final Gene create_item_minecraft_snowball = null;


	}

	public static void populateGeneList()
	{
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityHealth.class, "Health Boost", 20f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityStrength.class, "Strength Boost", 20f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityPunch.class, "Punch Boost", 20f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilitySprint.class, "Sprint Boost", 1f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityJumpBoost.class, "Jump Boost", 1f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityDamageResistance.class, "Resistance", 35f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityFallResistance.class, "Fall Resistance", 35f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityStepAssist.class, "Step Assist", 10f));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilityKnockbackResistance.class, "Knockback Resistance", 35f));

		GENE_REGISTRY.register(new GenePotionPunch("Poison Punch", Potion.getPotionById(19), 500, 3));
		GENE_REGISTRY.register(new GenePotionPunch("Slowness Punch", Potion.getPotionById(2), 500, 5));
		GENE_REGISTRY.register(new GenePotionPunch("Weakness Punch", Potion.getPotionById(18), 500, 2));
		GENE_REGISTRY.register(new GenePotionPunch("Nausea Punch", Potion.getPotionById(9), 250, 1));
		GENE_REGISTRY.register(new GenePotionPunch("Fatigue Punch", Potion.getPotionById(4), 500, 1));
		GENE_REGISTRY.register(new GenePotionPunch("Healing Punch", Potion.getPotionById(6), 1, 10));
		GENE_REGISTRY.register(new GenePotionPunch("Blindness Punch", Potion.getPotionById(15), 250, 1));
		GENE_REGISTRY.register(new GenePotionPunch("Hunger Punch", Potion.getPotionById(17), 500, 10));
		GENE_REGISTRY.register(new GenePotionPunch("Wither Punch", Potion.getPotionById(20), 400, 3));
		GENE_REGISTRY.register(new GenePotionPunch("Levitation Punch", Potion.getPotionById(25), 250, 10));

		GENE_REGISTRY.register(new GenePotionImmunity("Poison Immunity", Potion.getPotionById(19)));
		GENE_REGISTRY.register(new GenePotionImmunity("Slowness Immunity", Potion.getPotionById(2)));
		GENE_REGISTRY.register(new GenePotionImmunity("Weakness Immunity", Potion.getPotionById(18)));
		GENE_REGISTRY.register(new GenePotionImmunity("Nausea Immunity", Potion.getPotionById(9)));
		GENE_REGISTRY.register(new GenePotionImmunity("Fatigue Immunity", Potion.getPotionById(4)));
		GENE_REGISTRY.register(new GenePotionImmunity("Blindness Immunity", Potion.getPotionById(15)));
		GENE_REGISTRY.register(new GenePotionImmunity("Hunger Immunity", Potion.getPotionById(17)));
		GENE_REGISTRY.register(new GenePotionImmunity("Wither Immunity", Potion.getPotionById(20)));
		GENE_REGISTRY.register(new GenePotionImmunity("Levitation Immunity", Potion.getPotionById(25)));

		GENE_REGISTRY.register(new GeneNoQuality(AbilityFireResistance.class, "Fire Resistance"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilitySlowfall.class, "Slowfall"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilityWaterBreathing.class, "Water Breathing"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilityToughLungs.class, "Tough Lungs"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilityInvisibility.class, "Invisibility"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilityMakeHostile.class, "Hostile"));
		GENE_REGISTRY.register(new GeneNoQuality(AbilityMountable.class, "Mountable"));

		GENE_REGISTRY.register(new GeneEnergyBlast("Energy Blast", 25));
		GENE_REGISTRY.register(new GeneFlight(AbilityFlight.class, "Flight", 5));
		GENE_REGISTRY.register(new GeneHealing("Healing Factor", 5f));
		GENE_REGISTRY.register(new GeneTeleport("Teleportation", 50));
		GENE_REGISTRY.register(new GeneFirePunch("Fire Punch", 50));
		GENE_REGISTRY.register(new GeneScreech("Screech", 5f));
		GENE_REGISTRY.register(new GeneLayEgg("Lay Egg", 600));
		GENE_REGISTRY.register(new GeneExplode("Explode", 5f, 200));

		GENE_REGISTRY.register(new GeneEatBlock("Graze", Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), 8, 1f).setRegistryName(TheFifthWorld.MODID, "graze"));
		GENE_REGISTRY.register(new GeneEatBlock("Eat Hay", Blocks.HAY_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 16, 2f).setRegistryName(TheFifthWorld.MODID, "eat_hay"));

		GENE_REGISTRY.register(new GeneItemCreation("Create Slime",600, new ItemStack(Items.SLIME_BALL)));
		GENE_REGISTRY.register(new GeneItemCreation("Create Golden Sword",600, new ItemStack(Items.GOLDEN_SWORD)));
		GENE_REGISTRY.register(new GeneItemCreation("Create Gold Nugget",300, new ItemStack(Items.GOLD_NUGGET)));
		GENE_REGISTRY.register(new GeneItemCreation("Create Ghast Tear",600, new ItemStack(Items.GHAST_TEAR)));
		GENE_REGISTRY.register(new GeneItemCreation("Create Ender Pearl",300, new ItemStack(Items.ENDER_PEARL)));
		GENE_REGISTRY.register(new GeneItemCreation("Create Snowballs",30, new ItemStack(Items.SNOWBALL)));

		//	GENE_REGISTRY.register(new Gene(AbilitySizeChange.class, new int[] { 0 }, new Object[] { 5 }, "Size Changing"));

		//Defects
		GENE_REGISTRY.register(new GeneDefect(DefectExplosion.class, "Explodes").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(DefectButterFingers.class, "Butter Fingers").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(DefectBurning.class, "Spontaneous Combustion").setAlwaysOnChance(0.2f));
		GENE_REGISTRY.register(new GeneDefect(DefectDiet.class, "Can't Eat").setAlwaysOnChance(0.9f));
		GENE_REGISTRY.register(new GeneDefect(DefectEnable.class, "Enabling Power").setAlwaysOnChance(0.0f));
		GENE_REGISTRY.register(new GeneDefect(DefectDisable.class, "Disabling Power").setAlwaysOnChance(0.0f));

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
//			Nightvision
//			Hang upside down
//			Drain blood
//			Bite
//			Summon Bats
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
//			Milking
//			Charge
		}
		if (entity instanceof EntityMooshroom)
		{
			//Souping
			//Bonemeal effect
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
		}

		/*
		 * Neutral Mobs
		 */

		if (entity instanceof EntityLlama)
		{
			set.addGene(entity, LucraftCoreGenes.step_assist, GeneStrength.PERFECT.chance, r);
			//Spitting
		}
		if (entity instanceof EntityPolarBear)
		{
			set.addGene(entity, LucraftCoreGenes.strength, GeneStrength.VERY_HIGH.chance, r);
			//Fish in water
			//Claws
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
			//Bite
		}
		else if (entity instanceof EntitySpider)
		{
			//Wall Climbing
			//Web Attack
			//Bite
			set.addGene(entity, LucraftCoreGenes.invisibility, 1.0f);
			set.addGene(entity, FifthWorldGenes.mountable, 1.0f);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_poison, 1.0f);
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
			set.addGene(entity, LucraftCoreGenes.punch, GeneStrength.PERFECT.chance, r);
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
			// Fireball
			// Fire blasts
			// Smoke Form
			// Smoke Cloud
		}
		if (entity instanceof EntityCreeper)
		{
			set.addGene(entity, FifthWorldGenes.explode, GeneStrength.PERFECT.chance, r);
			//Explosion Punch
			//Electricity absorbtion
		}
		if (entity instanceof EntityElderGuardian)
		{
			set.addGene(entity, LucraftCoreGenes.energy_blast, GeneStrength.PERFECT.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_blindness, 1.0f);   //Visions
			//Long Range Potion effect
		}
		else if (entity instanceof EntityGuardian)
		{
			set.addGene(entity, LucraftCoreGenes.energy_blast, GeneStrength.VERY_HIGH.chance, r);
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_blindness, 1.0f);
			//Thorn projectiles
			//Predator Vision
			//Thorn when attacked
			//Lock on?
			//Laser (sustained)
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
			set.addGene(entity, FifthWorldGenes.potion_immunity_minecraft_instant_health, 1.0f);
			//Infect with superpower
			//Bite
			//Turn villagers into zombies
			//Death touch?
			//Immortality
			//Kill surrounding plant life
			//Kind of want to beat someone with my own limb?
			//Summon more zombies 
		}
		if (entity instanceof EntityMagmaCube)
		{
			set.addGene(entity, LucraftCoreGenes.fire_resistance, 1.0f);
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
			//Infinite arrows
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
		}

		//General
		if(entity instanceof EntityWaterMob){
			set.addGene(entity, LucraftCoreGenes.water_breathing, 1.0f);
		}
		if(entity instanceof EntityMob){
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

	public static ItemStack createGeneDataBook(ItemStack stack, EntityLivingBase attacker)
	{
		ItemStack s = new ItemStack(Items.WRITTEN_BOOK);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("title", "Gene Data");
		nbt.setString("author", attacker.getDisplayName().getFormattedText());
		NBTTagList pages = new NBTTagList();

		int index = 0;

		StringBuilder s2 = new StringBuilder();

		index = addLine(s2, pages, "Genetic Abilities:", index);
		for (NBTBase nbtBase : stack.getTagCompound().getCompoundTag(GeneSet.VIAL_DATA_TAG).getTagList(GeneSet.GENE_LIST_TAG, 10))
		{
			NBTTagCompound gene = (NBTTagCompound) nbtBase;
			Gene g = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(gene.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
			if (g != null)
			{

				index = addLine(s2, pages, "", index);
				index = addLine(s2, pages, g.displayName, index);
				index = addLine(s2, pages, Math.round(gene.getFloat(GeneSet.GENE_QUALITY_TAG) * 1000f) / 10.0 + "% Quality", index);
			}
		}
		index = addLine(s2, pages, "", index);
		index = addLine(s2, pages, "Genetic Defects", index);
		index = addLine(s2, pages, "", index);

		for (NBTBase nbtBase : stack.getTagCompound().getCompoundTag(GeneSet.VIAL_DATA_TAG).getTagList(GeneSet.DEFECT_LIST_TAG, 10))
		{
			NBTTagCompound gene = (NBTTagCompound) nbtBase;
			Gene g = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(gene.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
			if (g != null)
				index = addLine(s2, pages, g.displayName, index);
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

	//TODO use

	public static KarmaStat TEST_SUBJECTS_EXPLODED;

	@SubscribeEvent
	public static void onRegisterKarmaStats(RegistryEvent.Register<KarmaStat> e)
	{
		e.getRegistry()
				.register(TEST_SUBJECTS_EXPLODED = new KarmaStat("test_subjects_exploded", -25).setRegistryName(TheFifthWorld.MODID, "test_subjects_exploded"));
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof EntityPlayerMP && SuperpowerHandler
				.getSuperpower((EntityPlayer) event.getEntity()) instanceof SuperpowerGeneticallyModified)
			SuperpowerHandler.removeSuperpower((EntityPlayer) event.getEntity());
	}
}
