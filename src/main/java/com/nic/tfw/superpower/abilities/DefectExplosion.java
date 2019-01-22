package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/16/19.
 */
public class DefectExplosion extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectExplosion(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(!entity.world.isRemote && getCondition().isEnabled(entity, this))
			entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 5.0f, true);
	}

	@Override public void setCondition(Condition condition)
	{
		this.condition = condition;
	}

	@Override public Condition getCondition()
	{
		return this.condition;
	}

}
