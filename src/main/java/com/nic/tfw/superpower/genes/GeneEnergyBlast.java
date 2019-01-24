package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEnergyBlast;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/18/19.
 */

public class GeneEnergyBlast extends Gene
{

	public static final String COLOR_TAG = "color";
	private float maxDamage;
	public GeneEnergyBlast(String displayName, float maxDamage)
	{
		super(AbilityEnergyBlast.class, displayName);
		this.maxDamage = maxDamage;
	}


	@Override public void serializeExtra(GeneSet.GeneData geneData, NBTTagCompound compound)
	{
		super.serializeExtra(geneData, compound);
		compound.setIntArray(COLOR_TAG, getColor(geneData));
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		int[] c = getColor(geneData);
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, float.class, Vec3d.class).newInstance(entity, maxDamage*geneData.quality, new Vec3d(((double) c[0])/255.0, ((double) c[1])/255.0, ((double) c[2])/255.0));
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

	@Override float getQuality(Ability ability, Random r)
	{
		return (((AbilityEnergyBlast) ability).damage / maxDamage) + ((float) r.nextGaussian() * 0.25f);
	}
}
