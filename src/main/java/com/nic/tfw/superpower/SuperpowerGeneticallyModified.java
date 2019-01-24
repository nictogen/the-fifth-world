package com.nic.tfw.superpower;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.GeneSet;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerEntityHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
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
	public List<Ability> addDefaultAbilities(EntityLivingBase entity, List<Ability> list)
	{
		GeneticallyModifiedHandler handler = SuperpowerHandler.getSpecificSuperpowerEntityHandler(entity, GeneticallyModifiedHandler.class);

		if (handler.defaultAbilities.isEmpty())
			handler.defaultAbilities.addAll(new GeneSet(handler.getStyleNBTTag().getCompoundTag(GeneSet.VIAL_DATA_TAG)).toAbilities(entity));

		list.addAll(handler.defaultAbilities);

		return super.addDefaultAbilities(entity, list);
	}

	@Override public SuperpowerEntityHandler getNewSuperpowerHandler(ISuperpowerCapability cap)
	{
		return new GeneticallyModifiedHandler(cap, this);
	}

	public static class GeneticallyModifiedHandler extends SuperpowerEntityHandler
	{
		public List<Ability> defaultAbilities = new ArrayList<>();

		public GeneticallyModifiedHandler(ISuperpowerCapability cap, Superpower superpower)
		{
			super(cap, superpower);
		}

		@Override public void onUpdate()
		{
			super.onUpdate();
			if(defaultAbilities.isEmpty() || getAbilities().isEmpty()){
				GeneSet g = new GeneSet(getStyleNBTTag().getCompoundTag(GeneSet.VIAL_DATA_TAG));
				if(g.type == GeneSet.SetType.SERUM)
					cap.getSuperpower().getDefaultAbilities(getEntity(), getAbilities());
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
