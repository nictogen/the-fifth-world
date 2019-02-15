package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityPotionImmunity;
import net.minecraft.potion.Potion;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GenePotionImmunity extends Gene
{

	public GenePotionImmunity(String displayName, int potionID)
	{
		super(AbilityPotionImmunity.class, displayName, true);
		Potion potion = Potion.getPotionById(potionID);
		//noinspection ConstantConditions
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
		addDataMod(new DataMod<>(AbilityPotionImmunity.POTION, potion, false));
	}
}
