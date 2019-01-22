package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Nictogen on 1/18/19.
 */

public class GeneEnergyBlast extends Gene
{

	public static final String COLOR_TAG = "color";
	public GeneEnergyBlast(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName)
	{
		super(c, fields, maxValues, displayName);
	}

	@Override public Ability createAbilityInstance(EntityPlayer player, NBTTagCompound nbt) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		int[] color = nbt.getIntArray(COLOR_TAG);
		Vec3d vColor = color.length == 0 ? new Vec3d(1.0, 1.0, 1.0) : new Vec3d(color[0], color[1], color[2]);
		return ability.getAbilityClass().getConstructor(EntityPlayer.class, float.class, Vec3d.class).newInstance(player, 0f, vColor);
	}

	@Override public NBTTagCompound createAbilityTag(float quality)
	{
		return super.createAbilityTag(quality);
	}

	@Override public void combineGenes(NBTTagCompound one, NBTTagCompound two)
	{
		super.combineGenes(one, two);
		int[] c1 = one.getIntArray(COLOR_TAG);
		Color color1 = c1.length == 0 ? new Color(1.0f, 1.0f, 1.0f) : new Color(c1[0], c1[1], c1[2]);
		int[] c2 = two.getIntArray(COLOR_TAG);
		Color color2 = c2.length == 0 ? new Color(1.0f, 1.0f, 1.0f) : new Color(c2[0], c2[1], c2[2]);
		two.setIntArray(COLOR_TAG, new int[]{loop(color1.getRed() + color2.getRed()), loop(color1.getGreen()) + color2.getGreen(), loop(color1.getBlue() + color2.getBlue())});
	}

	private int loop(int num){
		if(num > 255){
			int over = 255 - num;
			return 255 - over;
		} else return num;
	}
}
