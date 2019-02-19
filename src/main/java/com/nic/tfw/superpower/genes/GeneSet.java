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
	public static final String GENE_LIST_TAG = "genes";
	public static final String VIAL_TYPE_TAG = "full";
	public static final String DONOR_LIST_TAG = "donors";
	public static final String CONDITION_LIST_TAG = "conditions";
	public static final String DONORS_TAG = "donors";
	public static final String VIAL_DATA_TAG = "vial_data";
	public static final String GENE_REGISTRY_NAME_TAG = "registry_name";
	public static final String GENE_QUALITY_TAG = "quality";

	public ArrayList<ArrayList<GeneData>> genes = new ArrayList<>();
	public UUID originalDonor = UUID.randomUUID();
	public SetType type;

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
		this.originalDonor = nbt.getUniqueId(DONORS_TAG);
	}

	public GeneSet(EntityLivingBase entityLivingBase)
	{
		this.type = SetType.SAMPLE;
		GeneHandler.addSpeciesGenes(entityLivingBase, this);
		//TOOD add existing genes
		Collections.shuffle(this.genes, getRandom());
		originalDonor = entityLivingBase.getPersistentID();
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
		compound.setTag(GENE_LIST_TAG, geneList);
		compound.setInteger(VIAL_TYPE_TAG, type.ordinal());
		compound.setUniqueId(DONORS_TAG, originalDonor);
		return compound;
	}

	public void addTo(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		assert stack.getTagCompound() != null;
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

		return abilityList;
	}

	public GeneSet toSerum(GeneSet bloodSample)
	{
		@SuppressWarnings("unchecked") ArrayList<ArrayList<GeneData>> g = (ArrayList<ArrayList<GeneData>>) genes.clone();

		ArrayList<UUID> donor = Lists.newArrayList(bloodSample.getDonors().iterator().next());
		for (ArrayList<GeneData> geneData : g)
			for (GeneData geneDatum : geneData)
				geneDatum.donors = donor;

		return new GeneSet(SetType.SERUM, g);
	}

	@SuppressWarnings("unchecked") public GeneSet combine(GeneSet otherSet)
	{

		//Create list of unique genes from set 1 and add to new final list
		ArrayList<GeneData> set1Genes = (ArrayList<GeneData>) genes.get(0).clone();
		set1Genes.removeIf(geneData1 -> otherSet.genes.get(0).stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));

		ArrayList<GeneData> finalGenes = new ArrayList<>(set1Genes);

		//Create list of unique genes from set 2 and add to final list
		ArrayList<GeneData> set2Genes = (ArrayList<GeneData>) otherSet.genes.get(0).clone();
		set2Genes.removeIf(geneData2 -> genes.get(0).stream().anyMatch(geneData1 -> geneData2.gene == geneData1.gene));
		finalGenes.addAll(set2Genes);

		//Create lists of non-unique genes from sets 1 and 2
		set1Genes = (ArrayList<GeneData>) genes.get(0).clone();
		set2Genes = (ArrayList<GeneData>) otherSet.genes.get(0).clone();

		set1Genes.removeIf(geneData1 -> finalGenes.stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));
		set2Genes.removeIf(geneData1 -> finalGenes.stream().anyMatch(geneData2 -> geneData1.gene == geneData2.gene));

		//Combine non-unique genes
		for (GeneData set1Gene : set1Genes)
			for (GeneData set2Gene : set2Genes)
				finalGenes.add(set1Gene.gene.combine(set1Gene, set2Gene));

		ArrayList<ArrayList<GeneData>> finalList = new ArrayList<>();
		finalList.add(finalGenes);
		//Create the new gene set with that information
		GeneSet finalSet = new GeneSet(type, finalList);

		//For each gene combined, give a chance of adding a new defect
		finalSet.createDefects();

		return finalSet;
	}

	/**
	 * Adders, getters, setters
	 */

	public void addGene(UUID donor, Gene g, float quality)
	{
		this.genes.add(Lists.newArrayList(new GeneData(g, Lists.newArrayList(donor), quality)));
	}

	public void addGene(UUID donor, Gene g, float qualityMax, Random random)
	{
		this.genes.add(Lists.newArrayList(new GeneData(g, Lists.newArrayList(donor), random.nextFloat() * qualityMax)));
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

		for (GeneData g : genes.get(0))
		{
			float q = g.quality;
			List<Gene> d = GeneHandler.GENE_REGISTRY.getValuesCollection().stream().filter(gene -> gene instanceof GeneDefect).collect(Collectors.toList());
			List<AbilityCondition.ConditionEntry> conditions = AbilityCondition.ConditionEntry.CONDITION_REGISTRY.getValuesCollection().stream()
					.filter(conditionEntry -> Objects.requireNonNull(conditionEntry.getRegistryName()).getNamespace().equals(TheFifthWorld.MODID))
					.collect(Collectors.toList());

			while (q > 0)
			{
				GeneDefect defect = (GeneDefect) d.get(r.nextInt(d.size()));
				float chance = Math.min(100, q);
				if (r.nextFloat() > (chance / 2.0f))
				{
					ArrayList<AbilityCondition.ConditionEntry> list = new ArrayList<>();
					AbilityCondition.ConditionEntry condition = r.nextFloat() > defect.getAlwaysOnChance() ?
							conditions.get(r.nextInt(conditions.size())) :
							Conditions.TheFifthWorldConditions.always_on;
					list.add(condition);

					genes.get(0).add(new GeneData(defect, g.donors, 1.0f, list));
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
		{
			a += uuid.getLeastSignificantBits() + uuid.getMostSignificantBits();
		}

		for (ArrayList<GeneData> gene : genes)
			for ( GeneData g : gene)
				a -= Objects.requireNonNull(g.gene.getRegistryName()).toString().length();

		return new Random(a);
	}

	/**
	 * Data class for specific genes, in order to store them in arraylist for easy access/serialization
	 */
	public static class GeneData
	{
		public ArrayList<UUID> donors = new ArrayList<>();
		public ArrayList<AbilityCondition.ConditionEntry> conditions = new ArrayList<>();
		public float quality;
		public Gene gene;
		public NBTTagCompound extra;

		public GeneData(Gene gene, ArrayList<UUID> donors, float quality, ArrayList<AbilityCondition.ConditionEntry> conditions)
		{
			this.gene = gene;
			this.donors.addAll(donors);
			this.quality = quality;
			this.extra = new NBTTagCompound();
			this.conditions = conditions;
		}

		public GeneData(Gene gene, ArrayList<UUID> donors, float quality)
		{
			this(gene, donors, quality, new ArrayList<>());
		}

		public GeneData(NBTTagCompound compound)
		{
			for (NBTBase nbtBase : compound.getTagList(GeneSet.DONOR_LIST_TAG, 8))
				donors.add(UUID.fromString(((NBTTagString) nbtBase).getString()));
			for (NBTBase nbtBase : compound.getTagList(GeneSet.CONDITION_LIST_TAG, 8))
				conditions.add(AbilityCondition.ConditionEntry.CONDITION_REGISTRY.getValue(new ResourceLocation(((NBTTagString) nbtBase).getString())));
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
			for (AbilityCondition.ConditionEntry condition : conditions)
				list.appendTag(new NBTTagString(condition.toString()));
			compound.setTag(GeneSet.DONOR_LIST_TAG, list);
			gene.serializeExtra(this, compound);
			return compound;
		}

		@Override public boolean equals(Object obj)
		{
			if (obj instanceof GeneData)
			{
				GeneData ob = (GeneData) obj;
				return ob.gene == gene && ob.conditions.equals(conditions);
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
