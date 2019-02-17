package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.IDefect;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/18/19.
 */
public class GeneDefect extends Gene
{
	private float alwaysOnChance = 0.0f;

	public GeneDefect(Class<? extends Ability> c, String displayName)
	{
		super(c, displayName);
	}

	public Ability getAbility(EntityLivingBase entity, GeneSet.DefectData defectData)
	{
		try
		{
			Ability ab = createAbilityInstance(entity, defectData);
			return ab;
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.DefectData defectData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		Ability a = ability.getAbilityClass().getConstructor(EntityLivingBase.class).newInstance(entity);
		if(a instanceof IDefect)
		{
			((IDefect) a).setCondition(defectData.condition.getConditionClass().newInstance());
		}
		return a;
	}

	public GeneDefect setAlwaysOnChance(float chance)
	{
		alwaysOnChance = chance;
		return this;
	}

	public float getAlwaysOnChance()
	{
		return alwaysOnChance;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 0;
	}
}
