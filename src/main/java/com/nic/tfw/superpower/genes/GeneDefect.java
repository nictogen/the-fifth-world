//package com.nic.tfw.superpower.genes;
//
//import com.nic.tfw.superpower.abilities.IDefect;
//import com.nic.tfw.superpower.conditions.Condition;
//import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.ResourceLocation;
//
//import java.lang.reflect.InvocationTargetException;
//
///**
// * Created by Nictogen on 1/18/19.
// */
//public class GeneDefect extends Gene
//{
//	private float alwaysOnChance = 0.0f;
//
//	public GeneDefect(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName)
//	{
//		super(c, fields, maxValues, displayName);
//	}
//
//	@Override public Ability createAbilityInstance(EntityLivingBase player, NBTTagCompound nbt)
//			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
//	{
//		Ability a = super.createAbilityInstance(player, nbt);
//		if(a instanceof IDefect)
//		{
//			Condition c = Condition.CONDITION_REGISTRY.getValue(new ResourceLocation(nbt.getString("condition")));
//			if (c != null)
//				((IDefect) a).setCondition(c);
//		}
//		return a;
//	}
//
//	public GeneDefect setAlwaysOnChance(float chance)
//	{
//		alwaysOnChance = chance;
//		return this;
//	}
//
//	public float getAlwaysOnChance()
//	{
//		return alwaysOnChance;
//	}
//
//}
