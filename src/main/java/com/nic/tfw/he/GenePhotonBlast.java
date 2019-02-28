package com.nic.tfw.he;

import com.nic.tfw.superpower.genes.Gene;
import com.nic.tfw.superpower.genes.GeneSet;
import lucraft.mods.heroesexpansion.abilities.AbilityPhotonBlast;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class GenePhotonBlast extends Gene
{
	private static final String COLOR_TAG = "color";

	public GenePhotonBlast()
	{
		super(AbilityPhotonBlast.class, "Photon Blast");
	}

	@Override public void serializeExtra(GeneSet.GeneData geneData, NBTTagCompound compound)
	{
		super.serializeExtra(geneData, compound);
		compound.setIntArray(COLOR_TAG, getColor(geneData));
	}

	@Override public Ability getAbility(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		Ability a = super.getAbility(entity, geneData);
		int[] color = getColor(geneData);
		a.getDataManager().set(
				AbilityPhotonBlast.COLOR, new Color(Math.min(255, Math.max(color[0], 0)), Math.min(255, Math.max(color[1], 0)), Math.min(255, Math.max(color[2], 0))));
		return a;
	}

	private int[] getColor(GeneSet.GeneData geneData)
	{
		return geneData.extra.hasKey(COLOR_TAG) ? geneData.extra.getIntArray(COLOR_TAG) : new int[] { 255, 255, 255 };
	}

	@Override public GeneSet.GeneData combine(GeneSet.GeneData one, GeneSet.GeneData two)
	{
		GeneSet.GeneData three = super.combine(one, two);
		int[] c1 = getColor(one);
		int[] c2 = getColor(two);
		three.extra.setIntArray(COLOR_TAG, new int[] { loop(c1[0] + c2[0]), loop(c1[1] + c2[1]), loop(c1[2] + c2[2]) });

		return three;
	}

	private int loop(int num)
	{
		if (num > 255)
		{
			int over = 255 - num;
			return 255 - over;
		}
		else
			return num;
	}

}
