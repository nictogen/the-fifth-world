package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilitySummonEntity;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class GeneSummonEntity extends Gene
{
	public GeneSummonEntity(String displayName, String entityName, int maxNumber, int maxCooldown)
	{
		super(AbilitySummonEntity.class, displayName,true);
		setRegistryName(ability.getRegistryName() + "_" + entityName.replace(':', '_'));
		this.addDataMod(new DataMod<>(AbilitySummonEntity.ENTITY_NAME, entityName, false));
		this.addDataMod(new DataMod<>(AbilitySummonEntity.NUMBER, maxNumber));
		this.addDataMod(new DataMod<>(Ability.MAX_COOLDOWN, maxCooldown, true, true));
	}
}
