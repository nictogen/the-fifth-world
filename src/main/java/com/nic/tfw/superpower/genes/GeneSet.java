package com.nic.tfw.superpower.genes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.potion.PotionExplode;
import com.nic.tfw.superpower.conditions.Conditions;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import lucraft.mods.lucraftcore.superpowers.capabilities.CapabilitySuperpower;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.nic.tfw.superpower.genes.GeneHandler.TEST_SUBJECTS_EXPLODED;

/**
 * Created by Nictogen on 1/23/19.
 */
public class GeneSet
{
	public static final String GENE_LIST_TAG = "genes";
	public static final String VIAL_TYPE_TAG = "full";
	public static final String DONOR_LIST_TAG = "donors";
	public static final String CONDITION_LIST_TAG = "conditions";
	public static final String INVERTED_CONDITIONS_TAG = "inverted";
	public static final String ORIGINAL_DONOR_UUID_TAG = "donors";
	public static final String ORIGINAL_DONOR_NAME_TAG = "donor_name";
	public static final String COLOR_TAG = "color";
	public static final String VIAL_DATA_TAG = "vial_data";
	public static final String GENE_REGISTRY_NAME_TAG = "registry_name";
	public static final String GENE_QUALITY_TAG = "quality";
	public static final String SHOW_DEFECTS_TAG = "show_defects";

	public ArrayList<ArrayList<GeneData>> genes = new ArrayList<>();
	public UUID originalDonor = UUID.randomUUID();
	public String originalDonorName = "";
	public SetType type;
	public boolean showDefects = false;

	/**
	 * Constructors/Converting from stuff
	 */

	public GeneSet(NBTTagCompound nbt)
	{
		this.type = SetType.values()[nbt.getInteger(VIAL_TYPE_TAG)];

		for (NBTBase nbtBase : nbt.getTagList(GENE_LIST_TAG, 9))
		{
			ArrayList<GeneData> list = new ArrayList<>();
			for (NBTBase base : ((NBTTagList) nbtBase))
				list.add(new GeneData((NBTTagCompound) base));
			genes.add(list);
		}

		this.originalDonor = nbt.getUniqueId(ORIGINAL_DONOR_UUID_TAG);
		this.originalDonorName = nbt.getString(ORIGINAL_DONOR_NAME_TAG);
		this.showDefects = nbt.getBoolean(SHOW_DEFECTS_TAG);
	}

	public GeneSet(EntityLivingBase entityLivingBase)
	{
		this.type = SetType.SAMPLE;
		GeneHandler.addSpeciesGenes(entityLivingBase, this);
		if (SuperpowerHandler.hasSuperpower(entityLivingBase, TheFifthWorld.Superpowers.genetically_modified))
		{
			ISuperpowerCapability cap = entityLivingBase.getCapability(CapabilitySuperpower.SUPERPOWER_CAP, null);
			if (cap != null && cap.getData() != null)
			{
				GeneSet g = new GeneSet(cap.getData().getCompoundTag(VIAL_DATA_TAG));
				if (!g.genes.isEmpty())
				{
					if(entityLivingBase instanceof EntityPlayer)
						g.createDefects();
					this.genes.add(g.genes.get(0));
				}
			}
		}
		this.originalDonor = entityLivingBase.getPersistentID();
		this.originalDonorName = entityLivingBase.getDisplayName().getFormattedText();
		Collections.shuffle(this.genes, getRandom());
	}

	public GeneSet(SetType type, ArrayList<ArrayList<GeneData>> genes)
	{
		this.type = type;
		this.genes = genes;
	}

	@Nullable public static GeneSet fromStack(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return null;
		if (!Objects.requireNonNull(stack.getTagCompound()).hasKey(GeneSet.VIAL_DATA_TAG))
			return null;
		return new GeneSet(stack.getTagCompound().getCompoundTag(GeneSet.VIAL_DATA_TAG));
	}

	/**
	 * Converting to stuff
	 */

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList geneList = new NBTTagList();
		for (ArrayList<GeneData> gene : genes)
		{
			NBTTagList gList = new NBTTagList();
			for (GeneData geneData : gene)
				gList.appendTag(geneData.serializeNBT());
			geneList.appendTag(gList);
		}
		compound.setUniqueId(ORIGINAL_DONOR_UUID_TAG, originalDonor);
		compound.setString(ORIGINAL_DONOR_NAME_TAG, originalDonorName);
		compound.setTag(GENE_LIST_TAG, geneList);
		compound.setInteger(VIAL_TYPE_TAG, type.ordinal());
		compound.setBoolean(SHOW_DEFECTS_TAG, showDefects);
		return compound;
	}

	public void addTo(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		assert stack.getTagCompound() != null;
		stack.getTagCompound().setTag(VIAL_DATA_TAG, serializeNBT());
		Random r = getRandom();
		Color c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
		stack.getTagCompound().setIntArray(COLOR_TAG, new int[] { c.getRed(), c.getGreen(), c.getBlue() });
	}

	public boolean giveTo(EntityLivingBase entity, @Nullable EntityPlayer injector)
	{
		if (type != SetType.SERUM)
			return false;
		if (SuperpowerHandler.hasSuperpower(entity) && !(entity instanceof EntityVillager))
			return false;
		if (!originalDonor.equals(entity.getPersistentID()))
			return false;

		for (ArrayList<GeneData> gene : this.genes)
		{
			gene.removeIf(geneData -> {
				if (geneData.gene instanceof GeneDefect)
				{
					float speciesChance = new Random(entity.getClass().getName().length()).nextFloat() * 0.5f;
					float indivChance =
							new Random(entity.getUniqueID().getLeastSignificantBits() + entity.getUniqueID().getMostSignificantBits()).nextFloat() * 0.5f;
					if (speciesChance + indivChance >= 0.95f)
					{
						Random r = entity.getRNG();
						if (entity.world instanceof WorldServer)
							for (int i = 0; i < 20; i++)
								((WorldServer) entity.world)
										.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, entity.posX + r.nextGaussian() * 0.2, entity.posY + r.nextGaussian(),
												entity.posZ + r.nextGaussian() * 0.2, 1, 0.0, 0.0, 0.0, 0.0);
						return true;
					}
				}
				return false;
			});

		}

		ISuperpowerCapability capability = SuperpowerHandler.getSuperpowerCapability(entity);
		capability.getData().setTag(VIAL_DATA_TAG, serializeNBT());

		SuperpowerHandler.setSuperpower(entity, TheFifthWorld.Superpowers.genetically_modified);

		if (injector != null && entity instanceof EntityVillager)
		{
			//Villager stuff
			ItemStack s = GeneHandler.createGeneDataBook(this, injector);
			MerchantRecipeList recipes = new MerchantRecipeList();
			recipes.add(new MerchantRecipe(new ItemStack(Items.PAPER), s));
			Field f = EntityVillager.class.getDeclaredFields()[7];
			f.setAccessible(true);
			try
			{
				f.set(entity, recipes);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			if (this.genes.stream().anyMatch(geneData -> geneData.stream().anyMatch(g -> g.gene instanceof GeneDefect)))
			{
				entity.addPotionEffect(new PotionEffect(PotionExplode.POTION_EXPLODE, 5 * 20));
				KarmaHandler.increaseKarmaStat(injector, TEST_SUBJECTS_EXPLODED);
			}
		}
		return true;
	}

	public Ability.AbilityMap toAbilities(EntityLivingBase entityLivingBase)
	{
		Ability.AbilityMap abilityList = new Ability.AbilityMap();
		for (ArrayList<GeneData> gene : genes)
		{
			for (GeneData geneData : gene)
			{
				if (geneData.gene != null)
				{
					Ability a = geneData.gene.getAbility(entityLivingBase, geneData);
					if (a != null)
					{
						abilityList.put("gene_" + Objects.requireNonNull(geneData.gene.getRegistryName()).toString(), a);
					}
				}
			}
		}

		for (ArrayList<GeneData> gene : genes)
			for (GeneData geneData : gene)
				if (geneData.gene != null)
					geneData.gene.postAbilityCreation(abilityList, geneData);
		return abilityList;
	}

	public GeneSet toSerum(GeneSet bloodSample)
	{
		GeneSet g = new GeneSet(SetType.SERUM, new ArrayList<>(genes));
		g.originalDonor = bloodSample.originalDonor;
		g.originalDonorName = bloodSample.originalDonorName;
		return g;
	}

	@SuppressWarnings("unchecked") public GeneSet mixSets(GeneSet otherSet)
	{
		//Combine
		ArrayList<GeneData> finalGenes = combine(otherSet);

		//Create defects if anything's changed
		if (!genes.get(0).containsAll(finalGenes) && !otherSet.genes.get(0).containsAll(finalGenes))
			createDefects();

		//Combine again, in order to combine defects
		finalGenes = combine(otherSet);

		ArrayList<ArrayList<GeneData>> finalList = new ArrayList<>();
		finalList.add(finalGenes);
		return new GeneSet(type, finalList);
	}

	public ArrayList<GeneData> combine(GeneSet otherSet)
	{
		ArrayList<GeneData> allGenes = new ArrayList<>(genes.get(0));
		ArrayList<GeneData> finalGenes = new ArrayList<>();
		allGenes.addAll(otherSet.genes.get(0));

		for (GeneData geneData : allGenes)
		{
			if (finalGenes.stream().noneMatch(g -> geneData.gene == g.gene))
			{
				ArrayList<GeneData> alikeGenes = new ArrayList<>(allGenes);
				alikeGenes.removeIf(g -> g.gene != geneData.gene);
				GeneData g = geneData;
				for (GeneData alikeGene : alikeGenes)
				{
					boolean b = true;
					//Check if it's from the same donor, in the case of non-defect genes
					if (!(g.gene instanceof GeneDefect))
						for (UUID donor : alikeGene.donors)
						{
							for (UUID uuid : g.donors)
							{
								if (uuid.equals(donor))
									b = false;
							}
						}
					if (b)
						g = g.gene.combine(g, alikeGene);
				}
				finalGenes.add(g);
			}
		}
		return finalGenes;
	}

	/**
	 * Adders, getters, setters
	 */

	public void addGene(UUID donor, Gene g, float quality)
	{
		if (g != null)
			this.genes.add(Lists.newArrayList(new GeneData(g, Sets.newHashSet(donor), quality)));
	}

	public void addGene(UUID donor, Gene g, float qualityMax, Random random)
	{
		if (g != null)
			this.genes.add(Lists.newArrayList(new GeneData(g, Sets.newHashSet(donor), random.nextFloat() * qualityMax)));
	}

	public void addGene(EntityLivingBase donor, Gene g, float quality)
	{
		addGene(donor.getPersistentID(), g, quality);
	}

	public void addGene(EntityLivingBase donor, Gene g, float qualityMax, Random random)
	{
		addGene(donor.getPersistentID(), g, qualityMax, random);
	}

	//TODO modify chance by intelligence ?
	public void createDefects()
	{
		Random r = getRandom();
		ArrayList<GeneData> listCopy = new ArrayList<>(genes.get(0));
		for (GeneData g : listCopy)
		{
			float q = g.quality;
			List<Gene> d = GeneHandler.GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene instanceof GeneDefect).collect(Collectors.toList());
			List<AbilityCondition.ConditionEntry> conditions = AbilityCondition.ConditionEntry.CONDITION_REGISTRY.getValuesCollection().stream()
					.filter(conditionEntry -> Objects.requireNonNull(conditionEntry.getRegistryName()).getNamespace().equals(TheFifthWorld.MODID))
					.collect(Collectors.toList());

			for (int i = 0; q > 0; i++)
			{
				GeneDefect defect = (GeneDefect) d.get(r.nextInt(d.size()));
				float chance = Math.min(1, q);
				if (r.nextFloat() <= (chance / Math.max(1.0f, 2f - 0.5f * i)))
				{
					HashSet<AbilityCondition.ConditionEntry> list = new HashSet<>();
					AbilityCondition.ConditionEntry condition = r.nextFloat() > defect.getAlwaysOnChance() ?
							conditions.get(r.nextInt(conditions.size())) :
							Conditions.TheFifthWorldConditions.always_on;
					list.add(condition);
					genes.get(0).add(new GeneData(defect, g.donors, 1.0f, list, (condition != Conditions.TheFifthWorldConditions.always_on) && r.nextBoolean()));
				}
				q -= chance;
			}
		}
	}

	public Set<UUID> getDonors()
	{
		HashSet<UUID> donors = new HashSet<>();
		donors.add(originalDonor);
		for (ArrayList<GeneData> gene : genes)
			for (GeneData geneData : gene)
				donors.addAll(geneData.donors);
		return donors;
	}

	public Random getRandom()
	{
		long a = 0;

		for (UUID uuid : getDonors())
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();

		for (ArrayList<GeneData> gene : genes)
			for (GeneData g : gene)
			{
				ResourceLocation r = g.gene.getRegistryName();
				if (r != null)
					a -= r.toString().length();
			}
		return new Random(a);
	}

	/**
	 * Data class for specific genes, in order to store them in arraylist for easy access/serialization
	 */
	public static class GeneData
	{
		public Set<UUID> donors = new HashSet<>();
		public Set<AbilityCondition.ConditionEntry> conditions = new HashSet<>();
		public float quality;
		public Gene gene;
		public NBTTagCompound extra;
		public boolean invertedConditions = false;

		public GeneData(Gene gene, Set<UUID> donors, float quality, Set<AbilityCondition.ConditionEntry> conditions)
		{
			this.gene = gene;
			this.donors.addAll(donors);
			this.quality = quality;
			this.extra = new NBTTagCompound();
			this.conditions = conditions;
		}

		public GeneData(Gene gene, Set<UUID> donors, float quality, Set<AbilityCondition.ConditionEntry> conditions, boolean invertedConditions)
		{
			this(gene, donors, quality, conditions);
			this.invertedConditions = invertedConditions;
		}

		public GeneData(Gene gene, Set<UUID> donors, float quality)
		{
			this(gene, donors, quality, new HashSet<>());
		}

		public GeneData(NBTTagCompound compound)
		{
			for (NBTBase nbtBase : compound.getTagList(GeneSet.DONOR_LIST_TAG, 8))
				donors.add(UUID.fromString(((NBTTagString) nbtBase).getString()));
			for (NBTBase nbtBase : compound.getTagList(GeneSet.CONDITION_LIST_TAG, 8))
			{
				AbilityCondition.ConditionEntry conditionEntry = AbilityCondition.ConditionEntry.CONDITION_REGISTRY
						.getValue(new ResourceLocation(((NBTTagString) nbtBase).getString()));
				if (conditionEntry != null)
					conditions.add(conditionEntry);
			}
			this.quality = compound.getFloat(GeneSet.GENE_QUALITY_TAG);
			this.gene = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(compound.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
			this.extra = compound;
			this.invertedConditions = compound.getBoolean(INVERTED_CONDITIONS_TAG);
		}

		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString(GeneSet.GENE_REGISTRY_NAME_TAG, gene.getRegistryName().toString());
			compound.setFloat(GeneSet.GENE_QUALITY_TAG, quality);
			NBTTagList list = new NBTTagList();
			for (UUID donor : donors)
				list.appendTag(new NBTTagString(donor.toString()));
			compound.setTag(GeneSet.DONOR_LIST_TAG, list);
			list = new NBTTagList();
			for (AbilityCondition.ConditionEntry condition : conditions)
				list.appendTag(new NBTTagString(condition.getRegistryName().toString()));
			compound.setTag(GeneSet.CONDITION_LIST_TAG, list);
			gene.serializeExtra(this, compound);
			compound.setBoolean(INVERTED_CONDITIONS_TAG, this.invertedConditions);
			return compound;
		}

		@Override public boolean equals(Object obj)
		{
			return super.equals(obj);
		}
	}

	public enum SetType
	{
		EMPTY,
		SAMPLE,
		GENE,
		SERUM
	}
}
