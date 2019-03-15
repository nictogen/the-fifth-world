package com.nic.tfw.superpower.genes;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityConditionNot;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityConditionOr;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;

/**
 * Created by Nictogen on 2019-03-09.
 */
public class GeneDisableDefect extends GeneDefect
{
	public GeneDisableDefect()
	{
		super(null, "Power Disabling", true);
		setRegistryName(TheFifthWorld.MODID, "disable_power");
	}

	@Override public Ability getAbility(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return null;
	}

	public void postAbilityCreation(EntityLivingBase entityLivingBase, Ability.AbilityMap abilityList, GeneSet.GeneData geneData)
	{
		ArrayList<AbilityCondition> list = new ArrayList<>();
		for (AbilityCondition.ConditionEntry condition : geneData.conditions)
		{
			try
			{
				list.add(condition.getConditionClass().newInstance());
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		if (!list.isEmpty())
			for (Ability value : abilityList.values())
			{

				value.addCondition(new AbilityConditionNot(new AbilityConditionOr(list.toArray(new AbilityCondition[0]))));
			}
	}

}
