package com.nic.tfw.superpower.conditions;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

/**
 * Created by Nictogen on 1/17/19.
 */
public class OneUseCondition extends Condition {

	private HashMap<EntityPlayer, Boolean> isEnabled = new HashMap<>();

	@Override public boolean isEnabled(EntityPlayer player, Ability ability)
	{
		if(isEnabled.get(player) != null && isEnabled.get(player)){
			isEnabled.put(player, false);
			return true;
		}
		return false;
	}

	public void setEnabled(EntityPlayer player)
	{
		isEnabled.put(player, true);
	}
}
