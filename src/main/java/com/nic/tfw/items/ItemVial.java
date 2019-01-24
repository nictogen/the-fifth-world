package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.GeneSet;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Created by Nictogen on 1/11/19.
 */
public class ItemVial extends Item
{
	public ItemVial()
	{
		setRegistryName(TheFifthWorld.MODID, "vial");
		setTranslationKey("vial");
		addPropertyOverride(new ResourceLocation("full"), (stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey(GeneSet.VIAL_DATA_TAG) ? stack.getTagCompound().getCompoundTag(
				GeneSet.VIAL_DATA_TAG).getInteger(GeneSet.VIAL_TYPE_TAG) : 0);
		setMaxStackSize(1);
	}

	public static class GeneColor implements IItemColor
	{
		@Override public int colorMultiplier(ItemStack stack, int tintIndex)
		{
			Color c = new Color(1.0f, 1.0f, 1.0f, 1.0f);

			GeneSet g = GeneSet.fromStack(stack);
			if (tintIndex == 1 && g != null)
			{
				Random r = g.getRandom();
				c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
			}
			return c.getRGB();
		}
	}

	@Override public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		GeneSet g = GeneSet.fromStack(stack);

		if(g != null && g.type == GeneSet.SetType.GENE)
		{
			tooltip.add("Genes: ");
			for (GeneSet.GeneData gene : g.genes)
			{
				String s = "";
				s += gene.gene.displayName;
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
				tooltip.add(s);
			}
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
