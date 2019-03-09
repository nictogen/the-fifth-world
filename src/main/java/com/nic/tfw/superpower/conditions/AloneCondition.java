package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * Created by Nictogen on 2019-03-09.
 */
public class AloneCondition extends AbilityCondition
{
	public AloneCondition()
	{
		super(ability -> {
			List<Entity> list = ability.getEntity().world.getEntitiesInAABBexcluding(ability.getEntity(), ability.getEntity().getEntityBoundingBox().grow(3), input -> input instanceof EntityLivingBase);
			return list.isEmpty();
		}, new TextComponentTranslation("condition.alone"));
	}
}
