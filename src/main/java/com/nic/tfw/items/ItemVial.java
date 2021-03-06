package com.nic.tfw.items;

import com.google.common.collect.Lists;
import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.Gene;
import com.nic.tfw.superpower.genes.GeneDefect;
import com.nic.tfw.superpower.genes.GeneHandler;
import com.nic.tfw.superpower.genes.GeneSet;
import lucraft.mods.lucraftcore.LucraftCore;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nictogen on 1/11/19.
 */
public class ItemVial extends Item
{
	public ItemVial()
	{
		setRegistryName(TheFifthWorld.MODID, "glass_vial");
		setTranslationKey("glass_vial");
		addPropertyOverride(new ResourceLocation("full"), (stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneSet.VIAL_DATA_TAG) ? stack.getTagCompound().getCompoundTag(
				GeneSet.VIAL_DATA_TAG).getInteger(GeneSet.VIAL_TYPE_TAG) : 0);
		setMaxStackSize(1);
		this.setCreativeTab(LucraftCore.CREATIVE_TAB);
	}

	public static class GeneColor implements IItemColor
	{
		@Override public int colorMultiplier(ItemStack stack, int tintIndex)
		{
			Color c = new Color(1.0f, 1.0f, 1.0f, 1.0f);
			if (tintIndex == 1 && stack.getTagCompound() != null && stack.getTagCompound().hasKey(GeneSet.COLOR_TAG))
			{
				int[] color = stack.getTagCompound().getIntArray(GeneSet.COLOR_TAG);
				c = new Color(color[0], color[1], color[2]);
			}
			return c.getRGB();
		}
	}

	@Override public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		GeneSet g = GeneSet.fromStack(stack);

		if(g != null )
		{
			switch (g.type){
			case EMPTY:
				return;
			case SAMPLE:
				tooltip.add("Blood Sample From: " + g.originalDonorName + ".");
				return;
			case GENE:
				break;
			case SERUM:
				tooltip.add("Serum Created For: " + g.originalDonorName + ".");
				break;
			}

			tooltip.add("Genes: ");
			for (ArrayList<GeneSet.GeneData> geneList : g.genes)
			{
				for (GeneSet.GeneData gene : geneList)
				{
					if (!(gene.gene instanceof GeneDefect) || g.showDefects)
					{
						String s = "";
						s += gene.gene.displayName;
						if (gene.gene.isQualified())
						{
							s += ", ";
							for (QualityRating qualityRating : QualityRating.values())
							{
								if (gene.quality >= qualityRating.minQuality)
								{
									s += qualityRating.displayName;
									break;
								}
							}
							s += " Quality";
						}
						tooltip.add(s);
					}
				}
			}
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (!isInCreativeTab(tab))
			return;

		subItems.add(new ItemStack(this));
		for (Gene gene : GeneHandler.GENE_REGISTRY)
		{
			ItemStack stack = new ItemStack(this);
			ArrayList<GeneSet.GeneData> data = Lists.newArrayList(new GeneSet.GeneData(gene, new HashSet<>(), 1.0f, new HashSet<>()));
			ArrayList<ArrayList<GeneSet.GeneData>> set = new ArrayList<>();
			set.add(data);
			GeneSet g = new GeneSet(GeneSet.SetType.GENE, set);
			g.originalDonor = UUID.randomUUID();
			g.showDefects = true;
			g.addTo(stack);
			subItems.add(stack);
		}
	}


	@Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		EnumHand off = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = playerIn.getHeldItem(handIn);
		ItemStack stackOff = playerIn.getHeldItem(off);
		if(stackOff.getItem() instanceof ItemInjectionGun) return super.onItemRightClick(worldIn, playerIn, handIn);
		if(playerIn.isSneaking() && stack.getItem() instanceof ItemVial)
			playerIn.getHeldItem(handIn).setTagCompound(new NBTTagCompound());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public enum QualityRating {
		OFF_THE_CHARTS("Off The Charts", 1.0f),
		EXCELLENT("Excellent", 0.85f),
		GOOD("Good", 0.675f),
		MEDIOCRE("Mediocre", 0.5f),
		INFERIOR("Inferior", 0.35f),
		POOR("Poor", 0.175f),
		USELESS("Useless", 0);

		String displayName;
		float minQuality;
		QualityRating(String displayName, float minQuality){
			this.displayName = displayName;
			this.minQuality = minQuality;
		}
	}
}
