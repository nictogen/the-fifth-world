package com.nic.tfw.superpower.abilities;

import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.SuperpowerEntityHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 1/17/19.
 */
public class DefectDisable extends AbilityConstant implements IDefect
{
	private Condition condition;

	public DefectDisable(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick() {
		SuperpowerEntityHandler handler = SuperpowerHandler.getSuperpowerEntityHandler(entity);
		for (Ability ability : handler.getAbilities())
		{
			if(!(ability instanceof IDefect))
				ability.setUnlocked(!condition.isEnabled(entity, this));
		}
		SuperpowerHandler.syncToPlayer(entity);
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
