package com.nic.tfw.superpower;

import com.nic.tfw.superpower.abilities.*;
import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nictogen on 1/12/19.
 */
@Mod.EventBusSubscriber
public class        Gene extends IForgeRegistryEntry.Impl<Gene>
{
	public static IForgeRegistry<Gene> GENE_REGISTRY;

	private static final UUID randomUUID = UUID.fromString("dc7bfdde-5e0e-44b8-ae43-20063182cd8b");
	public Ability.AbilityEntry ability;
	public int[] fields;
	public Object[] maxValues;
	public String displayName;

	public Gene(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName)
	{
		Optional<Ability.AbilityEntry> abilityEntry = Ability.ABILITY_REGISTRY.getValuesCollection().stream().filter(
				abilityEntry1 -> abilityEntry1.getAbilityClass() == c).findFirst();
		this.ability = abilityEntry.get();
		this.fields = fields;
		this.maxValues = maxValues;
		this.displayName = displayName;
		setRegistryName(ability.getRegistryName());
	}

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
		//		public static final Gene potion_punch = null;
		public static final Gene slowfall = null;
		public static final Gene energy_blast = null;
		public static final Gene fire_punch = null;
		public static final Gene flight = null;
		public static final Gene water_breathing = null;
		public static final Gene tough_lungs = null;
		public static final Gene invisibility = null;
	}

	@GameRegistry.ObjectHolder(LucraftCore.MODID)
	public static class DefectGenes
	{
		public static final Gene defect_explosion = null;
	}

	//TODO balancing
	public static void populateGeneList()
	{

		GENE_REGISTRY.register(new Gene(AbilityHealth.class, new int[] { -100 }, new Object[] { 2 }, "Health Boost"));
		GENE_REGISTRY.register(new Gene(AbilityHealing.class, new int[] { 0, 1 }, new Object[] { 20, 0.5f }, "Healing Factor"));
		GENE_REGISTRY.register(new Gene(AbilityStrength.class, new int[] { -100 }, new Object[] { 2.5f }, "Strength Boost"));
		GENE_REGISTRY.register(new Gene(AbilityPunch.class, new int[] { -100 }, new Object[] { 2.5f }, "Punch Boost"));
		GENE_REGISTRY.register(new Gene(AbilitySprint.class, new int[] { -100 }, new Object[] { 0.1f }, "Sprint Boost"));
		GENE_REGISTRY.register(new Gene(AbilityJumpBoost.class, new int[] { -100 }, new Object[] { 1f }, "Jump Boost"));
		GENE_REGISTRY.register(new Gene(AbilityDamageResistance.class, new int[] { -100 }, new Object[] { 4f }, "Resistance"));
		GENE_REGISTRY.register(new Gene(AbilityFallResistance.class, new int[] { -100 }, new Object[] { -5f }, "Fall Resistance"));
		GENE_REGISTRY.register(new Gene(AbilityFireResistance.class, new int[] {}, new Object[] {}, "Fire Res."));
		GENE_REGISTRY.register(new Gene(AbilityStepAssist.class, new int[] { -100 }, new Object[] { 1f }, "Step Assist"));
		GENE_REGISTRY.register(new Gene(AbilitySizeChange.class, new int[] { 0 }, new Object[] { 5 }, "Size Changing"));
		GENE_REGISTRY.register(new Gene(AbilityTeleport.class, new int[] { 0 }, new Object[] { 3 }, "Teleportation"));
		GENE_REGISTRY.register(new Gene(AbilityKnockbackResistance.class, new int[] { -100 }, new Object[] { 5f }, "Knockback Resistance"));
		//		GENE_REGISTRY.register(new Gene(AbilityPotionPunch.class, new int[]{-100}, new Object[]{5})); TODO
		GENE_REGISTRY.register(new Gene(AbilitySlowfall.class, new int[] {}, new Object[] {}, "Slowfall"));
		GENE_REGISTRY.register(new Gene(AbilityEnergyBlast.class, new int[] { 0 }, new Object[] { 2.5f }, "Energy Blast")); //TODO color
		GENE_REGISTRY.register(new Gene(AbilityFirePunch.class, new int[] { 0, -204 }, new Object[] { 5, 5 }, "Fire Punch"));
		GENE_REGISTRY.register(new Gene(AbilityFlight.class, new int[] { 0, 1 }, new Object[] { 0.5, 1 }, "Flight"));
		GENE_REGISTRY.register(new Gene(AbilityWaterBreathing.class, new int[] {}, new Object[] {}, "Water Breathing"));
		GENE_REGISTRY.register(new Gene(AbilityToughLungs.class, new int[] {}, new Object[] {}, "Tough Lungs"));
		GENE_REGISTRY.register(new Gene(AbilityInvisibility.class, new int[] {}, new Object[] {}, "Invisibility"));

		//Defects
		GENE_REGISTRY.register(new Gene(DefectExplosion.class, new int[] {}, new Object[] {}, "Explodes").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new Gene(DefectButterFingers.class, new int[] {}, new Object[] {}, "Butter Fingers").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new Gene(DefectBurning.class, new int[] {}, new Object[] {}, "Spontaneous Combustion").setAlwaysOnChance(0.2f));
		GENE_REGISTRY.register(new Gene(DefectDiet.class, new int[] {}, new Object[] {}, "Can't Eat").setAlwaysOnChance(0.9f));
		GENE_REGISTRY.register(new Gene(DefectEnable.class, new int[] {}, new Object[] {}, "Enabling Power").setAlwaysOnChance(0.0f));
		GENE_REGISTRY.register(new Gene(DefectDisable.class, new int[] {}, new Object[] {}, "Disabling Power").setAlwaysOnChance(0.0f));

	}

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event)
	{
		GENE_REGISTRY = new RegistryBuilder<Gene>().setName(new ResourceLocation(LucraftCore.MODID, "genes")).setType(Gene.class).setIDRange(0, 512).create();
	}

	public static void getRandomGenes(EntityLivingBase entity, ItemStack stack) throws IllegalAccessException
	{
		ArrayList<NBTTagCompound> list = new ArrayList<>();
		UUID donorUUID = entity.getUniqueID();
		Random r = new Random(donorUUID.getMostSignificantBits() + donorUUID.getLeastSignificantBits());

		NBTTagCompound vialCompound = new NBTTagCompound();
		//TODO specific entity genes
		if (entity instanceof EntityPlayer)
		{
			SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler((EntityPlayer) entity);
			if (handler != null && handler.superpower != null && !(handler.superpower instanceof SuperpowerGeneticallyModified))
				for (Ability ability : handler.getAbilities())
				{
					List<Gene> genes = GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene.ability == ability.getAbilityEntry())
							.collect(Collectors.toList());
					for (Gene gene : genes)
					{
						if (gene.fields.length == 0 || gene.maxValues.length == 0)
						{
							list.add(createAbilityTag(gene, 1.0f));
						}
						else {

							float quality = 0.0f;

							for (int i = 0; i < gene.fields.length; i++)
							{
								int n = gene.fields[i];
								Class c = ability.getAbilityEntry().getAbilityClass();
								if (n < 0)
								{
									while (n <= -100)
									{
										n += 100;
										c = c.getSuperclass();
									}
									n *= -1;
								}
								Field f = c.getDeclaredFields()[n];
								f.setAccessible(true);
								Object o = f.get(ability);
								if (o instanceof Integer)
								{
									quality += (int) o;
								}
								else if (o instanceof Double)
								{
									quality += (double) o;
								}
								else if (o instanceof Long)
								{
									quality += (double) o;
								}
								else if (o instanceof Float)
								{
									quality += (float) o;
								}
							}

							quality = quality / ((float) gene.fields.length);
							list.add(createAbilityTag(gene, quality + ((float) r.nextGaussian() * 0.25f)));
						}
					}
				}
		}

		if (entity instanceof EntityBat)
		{
			list.add(createAbilityTag(LucraftCoreGenes.flight, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityChicken)
		{
			list.add(createAbilityTag(LucraftCoreGenes.flight, r.nextFloat() * 0.1f));
			list.add(createAbilityTag(LucraftCoreGenes.slowfall, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.fall_resistance, r.nextFloat()));
		}

		if (entity instanceof EntityCow)
		{
			list.add(createAbilityTag(LucraftCoreGenes.health, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.resistance, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.knockback_resistance, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityDonkey)
		{
			list.add(createAbilityTag(LucraftCoreGenes.strength, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.jump_boost, r.nextFloat() * 0.5f));
		}
		else if (entity instanceof EntityMule)
		{
			list.add(createAbilityTag(LucraftCoreGenes.strength, r.nextFloat() * 0.75f));
			list.add(createAbilityTag(LucraftCoreGenes.jump_boost, r.nextFloat() * 0.25f));
		}
		else if (entity instanceof EntityHorse)
		{
			list.add(createAbilityTag(LucraftCoreGenes.strength, r.nextFloat() * 0.25f));
			list.add(createAbilityTag(LucraftCoreGenes.sprint, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.jump_boost, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityOcelot)
		{
			list.add(createAbilityTag(LucraftCoreGenes.sprint, r.nextFloat()));
		}

		if (entity instanceof EntityParrot)
		{
			list.add(createAbilityTag(LucraftCoreGenes.flight, r.nextFloat() * 0.75f));
		}

		if (entity instanceof EntityPig)
		{
			list.add(createAbilityTag(LucraftCoreGenes.health, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.healing, r.nextFloat() * 0.5f));
			list.add(createAbilityTag(LucraftCoreGenes.knockback_resistance, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityRabbit)
		{
			list.add(createAbilityTag(LucraftCoreGenes.jump_boost, r.nextFloat()));
			list.add(createAbilityTag(LucraftCoreGenes.sprint, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityWaterMob)
		{
			list.add(createAbilityTag(LucraftCoreGenes.water_breathing, 1.0f));
		}

		if (entity instanceof EntityWolf)
		{
			list.add(createAbilityTag(LucraftCoreGenes.strength, r.nextFloat()));
		}

		if (entity instanceof EntityBlaze)
		{
			list.add(createAbilityTag(LucraftCoreGenes.fire_resistance, 1.0f));
			list.add(createAbilityTag(LucraftCoreGenes.fire_punch, r.nextFloat() * 0.25f));
			list.add(createAbilityTag(LucraftCoreGenes.slowfall, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityCaveSpider)
		{
			//TODO potion punch poison
		}

		if (entity instanceof EntityCreeper)
		{
			//TODO explode
		}

		if (entity instanceof EntityEnderman)
		{
			list.add(createAbilityTag(LucraftCoreGenes.teleport, r.nextFloat()));
		}

		if (entity instanceof EntityEndermite)
		{
			list.add(createAbilityTag(LucraftCoreGenes.teleport, r.nextFloat() * 0.5f));
		}

		if (entity instanceof EntityGhast)
		{
			list.add(createAbilityTag(LucraftCoreGenes.fire_resistance, 1.0f));
			//TODO fireball
		}

		if (entity instanceof EntityIronGolem)
		{
			list.add(createAbilityTag(LucraftCoreGenes.resistance, r.nextFloat()));
			list.add(createAbilityTag(LucraftCoreGenes.knockback_resistance, r.nextFloat()));
			list.add(createAbilityTag(LucraftCoreGenes.strength, r.nextFloat() * 0.75f));
			list.add(createAbilityTag(LucraftCoreGenes.fall_resistance, r.nextFloat() * 0.75f));
		}

		if (entity instanceof EntityMagmaCube)
		{
			list.add(createAbilityTag(LucraftCoreGenes.fire_resistance, 1.0f));
			list.add(createAbilityTag(LucraftCoreGenes.fire_punch, r.nextFloat()));
		}

		if (entity instanceof EntityPigZombie)
		{
			list.add(createAbilityTag(LucraftCoreGenes.punch, r.nextFloat()));
		}

		if (entity instanceof EntitySlime)
		{
			list.add(createAbilityTag(LucraftCoreGenes.healing, r.nextFloat()));
			list.add(createAbilityTag(LucraftCoreGenes.health, r.nextFloat()));
		}

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		int defectIndex = 1;
		List<Gene> defects = GENE_REGISTRY.getValuesCollection().stream()
				.filter(gene -> Arrays.stream(gene.ability.getAbilityClass().getInterfaces()).anyMatch(aClass -> {
					return aClass.equals(IDefect.class);
				})).collect(Collectors.toList());
		List<Condition> conditions = new ArrayList<>(Condition.CONDITION_REGISTRY.getValuesCollection());
		for (int i = 1; i < 4 && !list.isEmpty(); i++)
		{
			int n = r.nextInt(list.size());
			vialCompound.setTag("gene_" + i, list.get(n));
			list.remove(n);

			//TODO modify chance by intelligence ?
			if (!defects.isEmpty() && r.nextFloat() >= 0.75)
			{
				n = r.nextInt(defects.size());
				NBTTagCompound nbt = createAbilityTag(defects.get(n), r.nextFloat());

				nbt.setString("condition", r.nextFloat() > defects.get(n).getAlwaysOnChance() ?
						conditions.get(r.nextInt(conditions.size())).getRegistryName().toString() :
						conditions.get(0).getRegistryName().toString());
				vialCompound.setTag("defect_" + defectIndex++, nbt);
				defects.remove(n);
			}
		}

		vialCompound.setInteger("full", 1);
		vialCompound.setUniqueId("donor_uuid", entity.getUniqueID());
		stack.getTagCompound().setTag("vial_tag", vialCompound);
	}

	public static NBTTagCompound createAbilityTag(Gene gene, float quality)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("registry_name", gene.ability.getRegistryName().toString());
		compound.setFloat("quality", quality);
		compound.setInteger("stacks", 1);
		return compound;
	}

	private float alwaysOnChance = 0.0f;

	public Gene setAlwaysOnChance(float chance)
	{
		alwaysOnChance = chance;
		return this;
	}

	public float getAlwaysOnChance()
	{
		return alwaysOnChance;
	}

	public Ability getAbility(EntityPlayer player, float quality, int stacks, @Nullable Condition condition)
	{
		try
		{
			Ability ab = ability.getAbilityClass().getConstructor(EntityPlayer.class).newInstance(player);
			if (ab instanceof AbilityAttributeModifier)
			{
				for (Constructor<?> constructor : ability.getAbilityClass().getConstructors())
				{
					if (constructor.getParameterCount() == 4)
					{
						ab = ability.getAbilityClass().getConstructor(EntityPlayer.class, UUID.class, float.class, int.class)
								.newInstance(player, randomUUID, 0f, 0);
					}
					else if (constructor.getParameterCount() == 3)
					{
						ab = ability.getAbilityClass().getConstructor(EntityPlayer.class, UUID.class, float.class).newInstance(player, randomUUID, 0f);
					}
				}
			} else if (ab instanceof AbilityEnergyBlast) {
				ab = ability.getAbilityClass().getConstructor(EntityPlayer.class, float.class, Vec3d.class).newInstance(player, 0f, new Vec3d(1.0f, 1.0f, 1.0f));
			}

			for (int i = 0; i < fields.length; i++)
			{
				int n = fields[i];
				Class c = ability.getAbilityClass();
				if (n < 0)
				{
					while (n <= -100)
					{
						n += 100;
						c = c.getSuperclass();
					}
					n *= -1;
				}
				//TODO all options of variables
				Field f = c.getDeclaredFields()[n];
				f.setAccessible(true);
				Object o = maxValues[i];
				if (o instanceof Integer || o instanceof Double || o instanceof Float || o instanceof Long)
				{
					if (o instanceof Integer)
						f.set(ab, (int) ((((Integer) o).floatValue()) * quality * (float) stacks));
					else if (o instanceof Double)
						f.set(ab, (double) ((((Double) o).floatValue()) * quality * (float) stacks));
					else if (o instanceof Long)
						f.set(ab, (long) ((((Long) o).floatValue()) * quality * (float) stacks));
					else
						f.set(ab, (Float) o * quality * (float) stacks);
				}
				else
					f.set(ab, maxValues[i]);
			}
			if (ab instanceof IDefect && condition != null)
			{
				((IDefect) ab).setCondition(condition);
			}
			return ab;
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

