package com.nic.tfw.superpower;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nictogen on 1/16/19.
 */
public class SuperpowerGeneticallyModified extends Superpower
{
	public static ResourceLocation texture = new ResourceLocation(TheFifthWorld.MODID, "textures/gui/dna.png");

	public SuperpowerGeneticallyModified()
	{
		super("genetically_modified");
		this.setRegistryName(TheFifthWorld.MODID, "genetically_modified");
	}

	@SideOnly(Side.CLIENT)
	@Override public void renderIcon(Minecraft mc, Gui gui, int x, int y)
	{
		mc.getTextureManager().bindTexture(texture);
		Gui.drawScaledCustomSizeModalRect(x, y, 0, 0, 512, 512, 32, 32, 512, 512);
		super.renderIcon(mc, gui, x, y);
	}

	@Override
	public List<Ability> addDefaultAbilities(EntityPlayer player, List<Ability> list)
	{
		GeneticallyModifiedHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, GeneticallyModifiedHandler.class);

		if (handler.defaultAbilities.isEmpty())
		{
			NBTTagCompound genes = handler.getStyleNBTTag().getCompoundTag("gene_data");
			for (int i = 1; genes.hasKey("gene_" + i); i++)
			{
				NBTTagCompound geneData = genes.getCompoundTag("gene_" + i);
				Gene g = Gene.GENE_REGISTRY.getValue(new ResourceLocation(geneData.getString("registry_name")));
				if (g != null)
				{
					Ability a = g.getAbility(player, geneData.getFloat("quality"), geneData.getInteger("stacks"), null);
					if (a != null)
					{
						a.setUnlocked(true);
						handler.defaultAbilities.add(a);
					}
				}
			}

			for (int i = 1; genes.hasKey("defect_" + i); i++)
			{
				NBTTagCompound geneData = genes.getCompoundTag("defect_" + i);
				Gene g = Gene.GENE_REGISTRY.getValue(new ResourceLocation(geneData.getString("registry_name")));
				if (g != null)
				{
					Ability a = g.getAbility(player, geneData.getFloat("quality"), geneData.getInteger("stacks"),
							Condition.CONDITION_REGISTRY.getValue(new ResourceLocation(geneData.getString("condition"))));
					if (a != null)
					{
						a.setUnlocked(true);
						a.setHidden(true);
						handler.defaultAbilities.add(a);
					}
				}
			}
		}

		list.addAll(handler.defaultAbilities);

		return super.addDefaultAbilities(player, list);
	}

	@Override public SuperpowerPlayerHandler getNewSuperpowerHandler(ISuperpowerCapability cap)
	{
		return new GeneticallyModifiedHandler(cap, this);
	}

	public static class GeneticallyModifiedHandler extends SuperpowerPlayerHandler
	{

		public List<Ability> defaultAbilities = new ArrayList<>();

		public GeneticallyModifiedHandler(ISuperpowerCapability cap, Superpower superpower)
		{
			super(cap, superpower);
		}

		@Override public void onUpdate(TickEvent.Phase phase)
		{
			super.onUpdate(phase);

			if((defaultAbilities.isEmpty() || getAbilities().isEmpty()) && getStyleNBTTag() != null && getStyleNBTTag().hasKey("gene_data") && getStyleNBTTag().getCompoundTag("gene_data").hasKey("gene_1")){
				cap.getSuperpower().getDefaultAbilities(getPlayer(), getAbilities());
			}
		}
	}

	@Override public boolean shouldAppearInHeroGuideBook()
	{
		return false;
	}

	@Override public GuiScreen getAbilityGui(EntityPlayer player)
	{
		return null; //TODO vague overview of genes?
	}
}
