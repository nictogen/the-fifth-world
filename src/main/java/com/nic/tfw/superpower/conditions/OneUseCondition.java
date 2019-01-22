package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;

/**
 * Created by Nictogen on 1/17/19.
 */
public class OneUseCondition extends Condition {

	private HashMap<EntityLivingBase, Boolean> isEnabled = new HashMap<>();

	@Override public boolean isEnabled(EntityLivingBase entity, Ability ability)
	{
		if(isEnabled.get(entity) != null && isEnabled.get(entity)){
			isEnabled.put(entity, false);
			return true;
		}
		return false;
	}

	public void setEnabled(EntityLivingBase player)
	{
		isEnabled.put(player, true);
	}
}
