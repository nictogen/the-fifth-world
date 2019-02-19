package com.nic.tfw.superpower;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.superpower.genes.GeneSet;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override public Ability.AbilityMap addDefaultAbilities(EntityLivingBase entity, Ability.AbilityMap abilities, Ability.EnumAbilityContext context)
	{
		abilities.putAll(new GeneSet(SuperpowerHandler.getSuperpowerCapability(entity).getData().getCompoundTag(GeneSet.VIAL_DATA_TAG)).toAbilities(entity));
		return abilities;
	}

	@Override public GuiScreen getAbilityGui(EntityPlayer player)
	{
		return null; //TODO vague overview of genes?
	}

	@Override public boolean shouldAddToTab(EntityPlayer player)
	{
		return false;
	}


}
