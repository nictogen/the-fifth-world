package com.nic.tfw.superpower.genes;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.potion.PotionExplode;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import com.nic.tfw.superpower.abilities.*;
import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.karma.KarmaStat;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.nic.tfw.items.ItemInjectionGun.removeContentsOfVial;

/**
 * Created by Nictogen on 1/18/19.
 */
@Mod.EventBusSubscriber
public class GeneHandler
{
	public static IForgeRegistry<Gene> GENE_REGISTRY;

	public static final UUID RANDOM_UUID = UUID.fromString("dc7bfdde-5e0e-44b8-ae43-20063182cd8b");
	public static final String GENE_LIST_TAG = "genes";
	public static final String DEFECT_LIST_TAG = "defects";
	public static final String CONDITION_TAG = "condition";
	public static final String VIAL_TYPE_TAG = "full";
	public static final String DONOR_LIST_TAG = "donors";
	public static final String VIAL_DATA_TAG = "vial_data";
	public static final String GENE_REGISTRY_NAME_TAG = "registry_name";
	public static final String GENE_QUALITY_TAG = "quality";

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

	public static void populateGeneList()
	{

		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityHealth.class, new int[] { -100 }, new Object[] { 20f }, "Health Boost"));
		GENE_REGISTRY.register(new Gene(AbilityHealing.class, new int[] { 1 }, new Object[] { 5f }, "Healing Factor"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityStrength.class, new int[] { -100 }, new Object[] { 20f }, "Strength Boost"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityPunch.class, new int[] { -100 }, new Object[] { 20f }, "Punch Boost"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifier(AbilitySprint.class, new int[] { -100 }, new Object[] { 1f }, "Sprint Boost"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityJumpBoost.class, new int[] { -100 }, new Object[] { 1f }, "Jump Boost"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityDamageResistance.class, new int[] { -100 }, new Object[] { 35f }, "Resistance"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityFallResistance.class, new int[] { -100 }, new Object[] { 35f }, "Fall Resistance"));
		GENE_REGISTRY.register(new Gene(AbilityFireResistance.class, new int[] {}, new Object[] {}, "Fire Res."));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityStepAssist.class, new int[] { -100 }, new Object[] { 10f }, "Step Assist"));
		GENE_REGISTRY.register(new Gene(AbilitySizeChange.class, new int[] { 0 }, new Object[] { 5 }, "Size Changing"));
		GENE_REGISTRY.register(new Gene(AbilityTeleport.class, new int[] { 0 }, new Object[] { 50 }, "Teleportation"));
		GENE_REGISTRY.register(new GeneAbilityAttributeModifierOperation(AbilityKnockbackResistance.class, new int[] { -100 }, new Object[] { 35f }, "Knockback Resistance"));
		GENE_REGISTRY.register(new Gene(AbilitySlowfall.class, new int[] {}, new Object[] {}, "Slowfall"));
		GENE_REGISTRY.register(new Gene(AbilityEnergyBlast.class, new int[] { 0 }, new Object[] { 20f }, "Energy Blast"));
		GENE_REGISTRY.register(new Gene(AbilityFirePunch.class, new int[] { 0, -204 }, new Object[] { 50, 50 }, "Fire Punch"));
		GENE_REGISTRY.register(new Gene(AbilityFlight.class, new int[] { 0, 1 }, new Object[] { 5.0, 10.0 }, "Flight"));
		GENE_REGISTRY.register(new Gene(AbilityWaterBreathing.class, new int[] {}, new Object[] {}, "Water Breathing"));
		GENE_REGISTRY.register(new Gene(AbilityToughLungs.class, new int[] {}, new Object[] {}, "Tough Lungs"));
		GENE_REGISTRY.register(new Gene(AbilityInvisibility.class, new int[] {}, new Object[] {}, "Invisibility"));

		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{3, 500}, "Poison Punch", Potion.getPotionById(19)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{5, 500}, "Slowness Punch", Potion.getPotionById(2)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{2, 500}, "Weakness Punch", Potion.getPotionById(18)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{1, 250}, "Nausea Punch", Potion.getPotionById(9)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{1, 500}, "Fatigue Punch", Potion.getPotionById(4)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{10, 1}, "Healing Punch", Potion.getPotionById(6)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{1, 250}, "Blindness Punch", Potion.getPotionById(15)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{10, 500}, "Hunger Punch", Potion.getPotionById(17)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{3, 400}, "Wither Punch", Potion.getPotionById(20)));
		GENE_REGISTRY.register(new GenePotionPunch(AbilityPotionPunch.class, new int[] {1, 2}, new Object[]{10, 250}, "Levitation Punch", Potion.getPotionById(25)));

		//Defects
		GENE_REGISTRY.register(new GeneDefect(DefectExplosion.class, new int[] {}, new Object[] {}, "Explodes").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(DefectButterFingers.class, new int[] {}, new Object[] {}, "Butter Fingers").setAlwaysOnChance(0.1f));
		GENE_REGISTRY.register(new GeneDefect(DefectBurning.class, new int[] {}, new Object[] {}, "Spontaneous Combustion").setAlwaysOnChance(0.2f));
		GENE_REGISTRY.register(new GeneDefect(DefectDiet.class, new int[] {}, new Object[] {}, "Can't Eat").setAlwaysOnChance(0.9f));
		GENE_REGISTRY.register(new GeneDefect(DefectEnable.class, new int[] {}, new Object[] {}, "Enabling Power").setAlwaysOnChance(0.0f));
		GENE_REGISTRY.register(new GeneDefect(DefectDisable.class, new int[] {}, new Object[] {}, "Disabling Power").setAlwaysOnChance(0.0f));

	}

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event)
	{
		GENE_REGISTRY = new RegistryBuilder<Gene>().setName(new ResourceLocation(LucraftCore.MODID, "genes")).setType(Gene.class).setIDRange(0, 512).create();
	}

	public static void getRandomGenes(EntityLivingBase entity, ItemStack stack)
	{
		ArrayList<NBTTagCompound> list = new ArrayList<>();
		UUID donorUUID = entity.getPersistentID();
		Random r = new Random(donorUUID.getMostSignificantBits() + donorUUID.getLeastSignificantBits());

		NBTTagCompound vialCompound = new NBTTagCompound();

		if (entity instanceof EntityPlayer)
		{
			SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler((EntityPlayer) entity);
			if (handler != null && handler.superpower != null && !(handler.superpower instanceof SuperpowerGeneticallyModified))
				for (Ability ability : handler.getAbilities())
					//TODO overrideable gene method to test if same ability
					for (Gene gene : GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene.ability == ability.getAbilityEntry()).collect(Collectors.toList()))
					{
						try
						{
							list.add(gene.createAbilityTagFromAbility(r, ability));
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
		}

		//TODO specific entity genes
		if (entity instanceof EntityBat)
		{
			list.add(LucraftCoreGenes.flight.createAbilityTag(r, GeneStrength.LOW));
		}

		if (entity instanceof EntityChicken)
		{

			list.add(LucraftCoreGenes.flight.createAbilityTag(r, GeneStrength.VERY_LOW));
			list.add(LucraftCoreGenes.slowfall.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.fall_resistance.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityCow)
		{
			list.add(LucraftCoreGenes.health.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.resistance.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.knockback_resistance.createAbilityTag(r, GeneStrength.MID));
		}

		if (entity instanceof EntityDonkey)
		{

			list.add(LucraftCoreGenes.strength.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.jump_boost.createAbilityTag(r, GeneStrength.MID));
		}
		else if (entity instanceof EntityMule)
		{
			list.add(LucraftCoreGenes.strength.createAbilityTag(r, GeneStrength.HIGH));
			list.add(LucraftCoreGenes.jump_boost.createAbilityTag(r, GeneStrength.LOW));
		}
		else if (entity instanceof EntityHorse)
		{
			list.add(LucraftCoreGenes.strength.createAbilityTag(r, GeneStrength.LOW));
			list.add(LucraftCoreGenes.sprint.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.jump_boost.createAbilityTag(r, GeneStrength.MID));
		}

		if (entity instanceof EntityOcelot)
		{
			list.add(LucraftCoreGenes.sprint.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityParrot)
		{
			list.add(LucraftCoreGenes.flight.createAbilityTag(r, GeneStrength.HIGH));
		}

		if (entity instanceof EntityPig)
		{
			list.add(LucraftCoreGenes.health.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.healing.createAbilityTag(r, GeneStrength.MID));
			list.add(LucraftCoreGenes.knockback_resistance.createAbilityTag(r, GeneStrength.MID));
		}

		if (entity instanceof EntityRabbit)
		{
			list.add(LucraftCoreGenes.jump_boost.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.sprint.createAbilityTag(r, GeneStrength.MID));
		}

		if (entity instanceof EntityWaterMob)
		{
			list.add(LucraftCoreGenes.water_breathing.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityWolf)
		{
			list.add(LucraftCoreGenes.strength.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityBlaze)
		{
			list.add(LucraftCoreGenes.fire_resistance.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.fire_punch.createAbilityTag(r, GeneStrength.LOW));
			list.add(LucraftCoreGenes.slowfall.createAbilityTag(r, GeneStrength.MID));
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
			list.add(LucraftCoreGenes.teleport.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityEndermite)
		{
			list.add(LucraftCoreGenes.teleport.createAbilityTag(r, GeneStrength.MID));
		}

		if (entity instanceof EntityGhast)
		{
			list.add(LucraftCoreGenes.fire_resistance.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.flight.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityIronGolem)
		{
			list.add(LucraftCoreGenes.resistance.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.knockback_resistance.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.strength.createAbilityTag(r, GeneStrength.HIGH));
			list.add(LucraftCoreGenes.fall_resistance.createAbilityTag(r, GeneStrength.HIGH));
		}

		if (entity instanceof EntityMagmaCube)
		{
			list.add(LucraftCoreGenes.fire_resistance.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.fire_punch.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (entity instanceof EntityPigZombie)
		{
			list.add(LucraftCoreGenes.punch.createAbilityTag(r, GeneStrength.VERY_HIGH));
		}

		if (entity instanceof EntitySlime)
		{
			list.add(LucraftCoreGenes.health.createAbilityTag(r, GeneStrength.PERFECT));
			list.add(LucraftCoreGenes.healing.createAbilityTag(r, GeneStrength.PERFECT));
		}

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		NBTTagList genes = new NBTTagList();

		NBTTagList donors = new NBTTagList();
		donors.appendTag(new NBTTagString(entity.getPersistentID().toString()));

		for (int i = 1; i < 4 && !list.isEmpty(); i++)
		{
			int n = r.nextInt(list.size());
			list.get(n).setTag(DONOR_LIST_TAG, donors);
			genes.appendTag(list.get(n));
			list.remove(n);
		}

		vialCompound.setTag(GENE_LIST_TAG, genes);
		vialCompound.setInteger(VIAL_TYPE_TAG, 1);
		vialCompound.setTag(DONOR_LIST_TAG, donors);

		stack.getTagCompound().setTag(VIAL_DATA_TAG, vialCompound);
	}

	public enum GeneStrength {
		NONE(0.0f),
		VERY_LOW(0.1f),
		LOW(0.2f),
		MID(0.3f),
		HIGH(0.4f),
		VERY_HIGH(0.5f),
		PERFECT(0.6f);

		public float chance;
		GeneStrength(float chance){
			this.chance = chance;
		}
	}

	public static ItemStack createGeneDataBook(ItemStack stack, EntityLivingBase attacker){
		ItemStack s = new ItemStack(Items.WRITTEN_BOOK);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("title", "Gene Data");
		nbt.setString("author", attacker.getDisplayName().getFormattedText());
		NBTTagList pages = new NBTTagList();

		int index = 0;

		StringBuilder s2 = new StringBuilder("");

		index = addLine(s2, pages, "Genetic Abilities:", index);
		for (NBTBase nbtBase : stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getTagList(GeneHandler.GENE_LIST_TAG, 10))
		{
			NBTTagCompound gene = (NBTTagCompound) nbtBase;
			Gene g = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(gene.getString(GeneHandler.GENE_REGISTRY_NAME_TAG)));
			if(g != null)
			{

				index = addLine(s2, pages, "", index);
				index = addLine(s2, pages, g.displayName, index);
				index = addLine(s2, pages, Math.round(gene.getFloat(GeneHandler.GENE_QUALITY_TAG)*1000f) / 10.0 + "% Quality", index);
			}
		}
		index = addLine(s2, pages, "", index);
		index = addLine(s2, pages, "Genetic Defects", index);
		index = addLine(s2, pages, "", index);

		for (NBTBase nbtBase : stack.getTagCompound().getCompoundTag(GeneHandler.VIAL_DATA_TAG).getTagList(GeneHandler.DEFECT_LIST_TAG, 10))
		{
			NBTTagCompound gene = (NBTTagCompound) nbtBase;
			Gene g = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(gene.getString(GeneHandler.GENE_REGISTRY_NAME_TAG)));
			if(g != null)
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

	private static int addLine(StringBuilder stringBuilder, NBTTagList pages, String content, int index){
		//TODO handle adding more than 20 characters
		if(index == 14) {
			pages.appendTag(new NBTTagString(stringBuilder.toString()));
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append(content);
			stringBuilder.append("\n");
			return 1;
		} else
			stringBuilder.append(content);
		stringBuilder.append("\n");
		return index + 1;
	}

	public static void giveSuperpowerFromInjectionGun(ItemStack stack, EntityPlayer player){
		if(SuperpowerHandler.getSuperpower(player) == null)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag(GeneHandler.VIAL_DATA_TAG, stack.getTagCompound().getTag(GeneHandler.VIAL_DATA_TAG));
			SuperpowerHandler.setSuperpower(player, TheFifthWorld.Superpowers.genetically_modified);
			SuperpowerGeneticallyModified.GeneticallyModifiedHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, SuperpowerGeneticallyModified.GeneticallyModifiedHandler.class);
			handler.setStyleNBTTag(compound);
			handler.defaultAbilities.clear();
			handler.getAbilities().clear();
			removeContentsOfVial(stack);
		}
	}

	public static void giveSuperpowerFromInjectionGun(ItemStack stack, EntityLivingBase injector, EntityVillager villager){
		ItemStack s = GeneHandler.createGeneDataBook(stack, injector);
		MerchantRecipeList recipes = new MerchantRecipeList();
		recipes.add(new MerchantRecipe(new ItemStack(Items.PAPER), s));
		Field f = EntityVillager.class.getDeclaredFields()[7];
		f.setAccessible(true);
		try
		{
			f.set(villager, recipes);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		villager.addPotionEffect(new PotionEffect(PotionExplode.POTION_EXPLODE, 5*20));
		if(injector instanceof EntityPlayer){
			KarmaHandler.increaseKarmaStat((EntityPlayer) injector, TEST_SUBJECTS_EXPLODED);
		}
		removeContentsOfVial(stack);
	}

	public static KarmaStat TEST_SUBJECTS_EXPLODED;

	@SubscribeEvent
	public static void onRegisterKarmaStats(RegistryEvent.Register<KarmaStat> e) {
		e.getRegistry().register(TEST_SUBJECTS_EXPLODED = new KarmaStat("test_subjects_exploded", -25).setRegistryName(TheFifthWorld.MODID, "test_subjects_exploded"));
	}

	public static Random getRandom(NBTTagCompound vialData){

		long a = 0;
		NBTTagList list = vialData.getTagList(GeneHandler.DONOR_LIST_TAG, 8);
		for (NBTBase nbtBase : list)
		{
			UUID uuid = UUID.fromString(((NBTTagString) nbtBase).getString());
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();
		}
		list = vialData.getTagList(GeneHandler.GENE_LIST_TAG, 10);
		for (NBTBase nbtBase : list)
		{
			NBTTagCompound compound = (NBTTagCompound) nbtBase;
			a -= compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG).length();
		}
		return new Random(a);
	}

	public static Random getRandom(NBTTagCompound vialData, NBTTagCompound vialData2){

		long a = 0;
		NBTTagList list = vialData.getTagList(GeneHandler.DONOR_LIST_TAG, 8);
		for (NBTBase nbtBase : list)
		{
			UUID uuid = UUID.fromString(((NBTTagString) nbtBase).getString());
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();
		}
		list = vialData.getTagList(GeneHandler.GENE_LIST_TAG, 10);
		for (NBTBase nbtBase : list)
		{
			NBTTagCompound compound = (NBTTagCompound) nbtBase;
			a -= compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG).length();
		}

		list = vialData2.getTagList(GeneHandler.DONOR_LIST_TAG, 8);
		for (NBTBase nbtBase : list)
		{
			UUID uuid = UUID.fromString(((NBTTagString) nbtBase).getString());
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();
		}
		list = vialData2.getTagList(GeneHandler.GENE_LIST_TAG, 10);
		for (NBTBase nbtBase : list)
		{
			NBTTagCompound compound = (NBTTagCompound) nbtBase;
			a -= compound.getString(GeneHandler.GENE_REGISTRY_NAME_TAG).length();
		}

		return new Random(a);
	}
	//TODO modify chance by intelligence ?
	public static ArrayList<NBTTagCompound> getDefects(Random r, float quality) {
		List<Gene> d = GeneHandler.GENE_REGISTRY.getValuesCollection().stream().filter(gene -> Arrays.stream(gene.ability.getAbilityClass().getInterfaces()).anyMatch(aClass -> { return aClass.equals(IDefect.class); })).collect(Collectors.toList());

		List<Condition> conditions = new ArrayList<>(Condition.CONDITION_REGISTRY.getValuesCollection());

		ArrayList<NBTTagCompound> defects = new ArrayList<>();

		while (quality > 0)
		{
			Gene defect = d.get(r.nextInt(d.size()));
			float chance = quality;
			if(chance > 100) chance = 100;
			if (r.nextFloat() > (chance / 2.0f))
			{
				NBTTagCompound nbt = defect.createAbilityTag(1.0f);

				nbt.setString(GeneHandler.CONDITION_TAG, r.nextFloat() > defect.getAlwaysOnChance() ?
						conditions.get(r.nextInt(conditions.size())).getRegistryName().toString() :
						conditions.get(0).getRegistryName().toString());

				defects.add(nbt);
			}
			quality -= chance;
		}

		return defects;
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event){
		if(event.getEntity() instanceof EntityPlayerMP && SuperpowerHandler.getSuperpower((EntityPlayer) event.getEntity()) instanceof SuperpowerGeneticallyModified)
			SuperpowerHandler.removeSuperpower((EntityPlayer) event.getEntity());
	}
}
