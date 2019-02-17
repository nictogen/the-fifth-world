package com.nic.tfw.superpower.genes;

import com.google.common.collect.Lists;
import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.potion.PotionExplode;
import com.nic.tfw.superpower.conditions.Conditions;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.nic.tfw.superpower.genes.GeneHandler.TEST_SUBJECTS_EXPLODED;

/**
 * Created by Nictogen on 1/23/19.
 */
public class GeneSet
{
	public static final String CONDITION_TAG = "condition";
	public static final String GENE_LIST_TAG = "genes";
	public static final String DEFECT_LIST_TAG = "defects";
	public static final String VIAL_TYPE_TAG = "full";
	public static final String DONOR_LIST_TAG = "donors";
	public static final String ORIGINAL_DONOR_TAG = "donors";
	public static final String VIAL_DATA_TAG = "vial_data";
	public static final String GENE_REGISTRY_NAME_TAG = "registry_name";
	public static final String GENE_QUALITY_TAG = "quality";

	public ArrayList<GeneData> genes = new ArrayList<>();
	public HashSet<DefectData> defects = new HashSet<>();
	public UUID originalDonor = UUID.randomUUID();
	public SetType type;

	/**
	 * Constructors/Converting from stuff
	 */

	//TODO superpower constructor
	public GeneSet(NBTTagCompound nbt)
	{
		this.type = SetType.values()[nbt.getInteger(VIAL_TYPE_TAG)];
		for (NBTBase nbtBase : nbt.getTagList(GENE_LIST_TAG, 10))
			genes.add(new GeneData((NBTTagCompound) nbtBase));
		for (NBTBase nbtBase : nbt.getTagList(DEFECT_LIST_TAG, 10))
			defects.add(new DefectData((NBTTagCompound) nbtBase));
		this.originalDonor = nbt.getUniqueId(ORIGINAL_DONOR_TAG);
	}

	public GeneSet(EntityLivingBase entityLivingBase)
	{
		this.type = SetType.SAMPLE;
		GeneHandler.addSpeciesGenes(entityLivingBase, this);
		Collections.shuffle(this.genes, getRandom());
		this.genes = new ArrayList<>(this.genes.subList(0, (this.genes.size() < 3) ? this.genes.size() : 3));
		originalDonor = entityLivingBase.getPersistentID();
	}

	public GeneSet(SetType type, ArrayList<GeneData> genes, HashSet<DefectData> defects)
	{
		this.type = type;
		this.genes = genes;
		this.defects = defects;
	}

	@Nullable public static GeneSet fromStack(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return null;
		if (!stack.getTagCompound().hasKey(GeneSet.VIAL_DATA_TAG))
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
		for (GeneData gene : genes)
			geneList.appendTag(gene.serializeNBT());
		compound.setTag(GENE_LIST_TAG, geneList);
		NBTTagList defectList = new NBTTagList();
		for (DefectData defect : defects)
			defectList.appendTag(defect.serializeNBT());
		compound.setTag(DEFECT_LIST_TAG, defectList);
		compound.setInteger(VIAL_TYPE_TAG, type.ordinal());
		compound.setUniqueId(ORIGINAL_DONOR_TAG, originalDonor);
		return compound;
	}

	public void addTo(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag(VIAL_DATA_TAG, serializeNBT());
	}

	public boolean giveTo(EntityLivingBase entity, @Nullable EntityPlayer injector)
	{
		if (type != SetType.SERUM)
			return false;
		if (SuperpowerHandler.hasSuperpower(entity))
			return false;
		if (!getDonors().contains(entity.getPersistentID()))
			return false;

		ISuperpowerCapability capability = SuperpowerHandler.getSuperpowerCapability(entity);
		capability.getData().setTag(VIAL_DATA_TAG, serializeNBT());

		SuperpowerHandler.setSuperpower(entity, TheFifthWorld.Superpowers.genetically_modified);

		if(injector != null && entity instanceof EntityVillager)
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
			if(!this.defects.isEmpty())
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
		for (GeneData gene : genes)
		{
			if (gene.gene != null)
			{
				Ability a = gene.gene.getAbility(entityLivingBase, gene);
				if (a != null)
				{
					abilityList.put("gene_" + gene.gene.getRegistryName().toString(), a);
				}
			}
		}

		for (DefectData defect : defects)
		{
			if (defect.gene instanceof GeneDefect)
			{
				Ability a = ((GeneDefect) defect.gene).getAbility(entityLivingBase, defect);
				if (a != null)
				{
					abilityList.put("defect_" + defect.gene.getRegistryName().toString(), a);
				}
			}
		}

		return abilityList;
	}

	public GeneSet toSerum(GeneSet bloodSample)
	{
		ArrayList<GeneData> g = (ArrayList<GeneData>) genes.clone();
		ArrayList<UUID> donor = Lists.newArrayList(bloodSample.getDonors().iterator().next());
		for (GeneData gene : g)
			gene.donors = donor;

		return new GeneSet(SetType.SERUM, g, defects);
	}

	public GeneSet combine(GeneSet otherSet)
	{

		//Create final gene list
		ArrayList<GeneData> finalGenes = new ArrayList<>();

		//Create list of unique genes from set 1 and add to final list
		ArrayList<GeneData> set1Genes = (ArrayList<GeneData>) genes.clone();
		set1Genes.removeIf(geneData1 -> otherSet.genes.stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));
		finalGenes.addAll(set1Genes);

		//Create list of unique genes from set 2 and add to final list
		ArrayList<GeneData> set2Genes = (ArrayList<GeneData>) otherSet.genes.clone();
		set2Genes.removeIf(geneData2 -> genes.stream().anyMatch(geneData1 -> geneData2.gene == geneData1.gene));
		finalGenes.addAll(set2Genes);

		//Create lists of non-unique genes from sets 1 and 2
		set1Genes = (ArrayList<GeneData>) genes.clone();
		set2Genes = (ArrayList<GeneData>) otherSet.genes.clone();

		set1Genes.removeIf(geneData1 -> finalGenes.stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));
		set2Genes.removeIf(geneData1 -> finalGenes.stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));

		//Combine non-unique genes
		for (GeneData set1Gene : set1Genes)
			for (GeneData set2Gene : set2Genes)
				finalGenes.add(set1Gene.gene.combine(set1Gene, set2Gene));

		//Combine defect lists
		HashSet<DefectData> finalDefects = new HashSet<>();
		finalDefects.addAll(defects);
		finalDefects.addAll(otherSet.defects);

		//Create the new gene set with that information
		GeneSet finalSet = new GeneSet(type, finalGenes, finalDefects);

		//For each gene combined, give a chance of adding a new defect
		finalSet.createDefects();

		return finalSet;
	}

	/**
	 * Adders, getters, setters
	 */

	public void addGene(UUID donor, Gene g, float quality)
	{
		this.genes.add(new GeneData(g, Lists.newArrayList(donor), quality));
	}

	public void addGene(UUID donor, Gene g, float qualityMax, Random random)
	{
		this.genes.add(new GeneData(g, Lists.newArrayList(donor), random.nextFloat() * qualityMax));
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

		for (GeneData g : genes)
		{
			float q = g.quality;
			List<Gene> d = GeneHandler.GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene instanceof GeneDefect).collect(Collectors.toList());
			List<AbilityCondition.ConditionEntry> conditions = AbilityCondition.ConditionEntry.CONDITION_REGISTRY.getValuesCollection().stream().filter(conditionEntry -> Objects.requireNonNull(conditionEntry.getRegistryName()).getNamespace().equals(TheFifthWorld.MODID)).collect(Collectors.toList());

			while (q > 0)
			{
				GeneDefect defect = (GeneDefect) d.get(r.nextInt(d.size()));
				float chance = Math.min(100, q);
				if (r.nextFloat() > (chance / 2.0f))
					defects.add(new DefectData(defect, r.nextFloat() > defect.getAlwaysOnChance() ? conditions.get(r.nextInt(conditions.size())) : Conditions.TheFifthWorldConditions.always_on));
				q -= chance;
			}
		}
	}

	public Set<UUID> getDonors()
	{
		HashSet<UUID> donors = new HashSet<>();
		donors.add(originalDonor);
		for (GeneData gene : genes)
			donors.addAll(gene.donors);
		return donors;
	}

	public Random getRandom()
	{
		long a = 0;

		for (UUID uuid : getDonors())
		{
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();
		}

		for (GeneData gene : genes)
		{
			a -= gene.gene.getRegistryName().toString().length();
		}

		return new Random(a);
	}

	/**
	 * Data class for specific genes, in order to store them in arraylist for easy access/serialization
	 */
	public static class GeneData
	{
		public ArrayList<UUID> donors = new ArrayList<>();
		public float quality;
		public Gene gene;
		public NBTTagCompound extra;

		public GeneData(Gene gene, ArrayList<UUID> donors, float quality)
		{
			this.gene = gene;
			this.donors.addAll(donors);
			this.quality = quality;
			this.extra = new NBTTagCompound();
		}

		public GeneData(NBTTagCompound compound)
		{
			for (NBTBase nbtBase : compound.getTagList(GeneSet.DONOR_LIST_TAG, 8))
				donors.add(UUID.fromString(((NBTTagString) nbtBase).getString()));
			this.quality = compound.getFloat(GeneSet.GENE_QUALITY_TAG);
			this.gene = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(compound.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
			this.extra = compound;
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
			gene.serializeExtra(this, compound);
			return compound;
		}

	}

	public static class DefectData
	{
		public Gene gene;
		public AbilityCondition.ConditionEntry condition;

		public DefectData(Gene gene, AbilityCondition.ConditionEntry condition)
		{
			this.gene = gene;
			this.condition = condition;
		}

		public DefectData(NBTTagCompound compound)
		{
			this.gene = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(compound.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
			this.condition = AbilityCondition.ConditionEntry.CONDITION_REGISTRY.getValue(new ResourceLocation(compound.getString(CONDITION_TAG)));
		}

		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString(GeneSet.GENE_REGISTRY_NAME_TAG, gene.getRegistryName().toString());
			compound.setString(CONDITION_TAG, condition.getRegistryName().toString());
			return compound;
		}

		@Override public boolean equals(Object obj)
		{
			if (obj instanceof DefectData)
			{
				DefectData ob = (DefectData) obj;
				return ob.condition == condition && ob.gene == gene;
			}
			else
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
