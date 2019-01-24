package com.nic.tfw.superpower.genes;

import com.google.common.collect.Lists;
import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

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
	public ArrayList<Gene> defects = new ArrayList<>();
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
		this.originalDonor = nbt.getUniqueId(ORIGINAL_DONOR_TAG);
		//TODO defects
	}

	public GeneSet(EntityLivingBase entityLivingBase)
	{
		this.type = SetType.SAMPLE;
		GeneHandler.addSpeciesGenes(entityLivingBase, this);
		originalDonor = entityLivingBase.getPersistentID();
	}

	public GeneSet(SetType type, ArrayList<GeneData> genes, ArrayList<Gene> defects)
	{
		this.type = type;
		this.genes = genes;
		this.defects = defects;
	}

	@Nullable public static GeneSet fromStack(ItemStack stack){
		if(!stack.hasTagCompound()) return null;
		if(!stack.getTagCompound().hasKey(GeneSet.VIAL_DATA_TAG)) return null;
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

	public boolean giveTo(EntityLivingBase entity)
	{
		if(type != SetType.SERUM) return false;
		if(SuperpowerHandler.hasSuperpower(entity)) return false;
		if(!getDonors().contains(entity.getPersistentID())) return false;
		SuperpowerHandler.setSuperpower(entity, TheFifthWorld.Superpowers.genetically_modified);
		SuperpowerGeneticallyModified.GeneticallyModifiedHandler handler = SuperpowerHandler.getSpecificSuperpowerEntityHandler(entity, SuperpowerGeneticallyModified.GeneticallyModifiedHandler.class);
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag(VIAL_DATA_TAG, serializeNBT());
		handler.setStyleNBTTag(compound);
		handler.defaultAbilities.clear();
		handler.getAbilities().clear();
		return true;
	}

	public ArrayList<Ability> toAbilities(EntityLivingBase entityLivingBase){

		ArrayList<Ability> abilityList = new ArrayList<>();
		for (GeneData gene : genes)
		{
			if(gene.gene != null)
			{
				Ability a = gene.gene.getAbility(entityLivingBase, gene);
				if (a != null)
				{
					a.setUnlocked(true);
					abilityList.add(a);
				}
			}
		}

//		for (NBTBase nbtBase : genes.getTagList(GeneSet.DEFECT_LIST_TAG, 10))
//		{
//			NBTTagCompound compound = (NBTTagCompound) nbtBase;
//			Gene g = GeneHandler.GENE_REGISTRY.getValue(new ResourceLocation(compound.getString(GeneSet.GENE_REGISTRY_NAME_TAG)));
//			if (g != null)
//			{
//				Ability a = g.getAbility(entity, 1.0f, compound);
//				if (a != null)
//				{
//					a.setUnlocked(true);
//					a.setHidden(true);
//					handler.defaultAbilities.add(a);
//				}
//			}
//		}
		return abilityList;
	}

	public GeneSet toSerum(GeneSet bloodSample){
		ArrayList<GeneData> g = (ArrayList<GeneData>) genes.clone();
		ArrayList<UUID> donor =  Lists.newArrayList(bloodSample.getDonors().iterator().next());
		for (GeneData gene : g)
			gene.donors = donor;

		return new GeneSet(SetType.SERUM, g, new ArrayList<>());
	}

	public GeneSet combine(GeneSet otherSet){

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

		//TODO defects

		return new GeneSet(type, finalGenes, new ArrayList<>());
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

	public Set<UUID> getDonors() {
		HashSet<UUID> donors = new HashSet<>();
		donors.add(originalDonor);
		for (GeneData gene : genes)
			donors.addAll(gene.donors);
		return donors;
	}

	public Random getRandom()
	{
		return new Random(getRandomLong());
	}

	public Random getRandom(GeneSet otherSet)
	{
		return new Random(getRandomLong() + otherSet.getRandomLong());
	}

	private long getRandomLong()
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

		return a;
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

	public enum SetType
	{
		EMPTY,
		SAMPLE,
		GENE,
		SERUM
	}
}
