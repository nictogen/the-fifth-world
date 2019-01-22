package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * Created by Nictogen on 1/18/19.
 */
public class GeneAbilityAttributeModifier extends Gene
{
	public GeneAbilityAttributeModifier(Class<? extends Ability> c, int[] fields, Object[] maxValues,
			String displayName)
	{
		super(c, fields, maxValues, displayName);
	}

	@Override public Ability createAbilityInstance(EntityLivingBase player, NBTTagCompound nbt)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, UUID.class, float.class).newInstance(player, GeneHandler.RANDOM_UUID, 0f);
	}
}
