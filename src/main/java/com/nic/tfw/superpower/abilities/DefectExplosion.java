package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/16/19.
 */
public class DefectExplosion extends AbilityConstant implements IDefect
{
	private AbilityCondition condition;

	public DefectExplosion(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && getCondition().test(this))
			entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 5.0f, true);
	}

	@Override public void setCondition(AbilityCondition condition)
	{
		this.condition = condition;
	}

	@Override public AbilityCondition getCondition()
	{
		return this.condition;
	}

}
