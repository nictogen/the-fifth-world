package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Nictogen on 1/18/19.
 */
//TODO color
public class GeneEnergyBlast extends Gene
{
	public GeneEnergyBlast(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName)
	{
		super(c, fields, maxValues, displayName);
	}

	@Override public Ability createAbilityInstance(EntityPlayer player, NBTTagCompound nbt)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityPlayer.class, float.class, Vec3d.class).newInstance(player, 0f, new Vec3d(1.0f, 1.0f, 1.0f));
	}
}
