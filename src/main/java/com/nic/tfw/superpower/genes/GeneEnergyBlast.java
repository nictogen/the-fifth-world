package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEnergyBlast;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;

/**
 * Created by Nictogen on 1/18/19.
 */

public class GeneEnergyBlast extends Gene
{

	private static final String COLOR_TAG = "color";

	public GeneEnergyBlast(String displayName, float maxDamage)
	{
		super(AbilityEnergyBlast.class, displayName);
		addDataMod(new DataMod<>(AbilityEnergyBlast.DAMAGE, maxDamage));
	}


	@Override public void serializeExtra(GeneSet.GeneData geneData, NBTTagCompound compound)
	{
		super.serializeExtra(geneData, compound);
		compound.setIntArray(COLOR_TAG, getColor(geneData));
	}

	@Override Ability getAbility(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		Ability a = super.getAbility(entity, geneData);
		int[] color = getColor(geneData);
		a.getDataManager().set(AbilityEnergyBlast.COLOR, new Color(color[0], color[1], color[2]));
		return a;
	}

	private int[] getColor(GeneSet.GeneData geneData){
		return geneData.extra.hasKey(COLOR_TAG) ? geneData.extra.getIntArray(COLOR_TAG) : new int[]{255, 255, 255};
	}

	@Override public GeneSet.GeneData combine(GeneSet.GeneData one, GeneSet.GeneData two)
	{
		GeneSet.GeneData three = super.combine(one, two);
		int[] c1 = getColor(one);
		int[] c2 = getColor(two);
		three.extra.setIntArray(COLOR_TAG,new int[]{loop(c1[0] + c2[0]), loop(c1[1] + c2[1]), loop(c1[2] + c2[2])} );

		return three;
	}

	private int loop(int num){
		if(num > 255){
			int over = 255 - num;
			return 255 - over;
		} else return num;
	}

}
