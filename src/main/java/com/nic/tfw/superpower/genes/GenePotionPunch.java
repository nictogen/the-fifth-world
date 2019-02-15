package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityPotionPunch;
import net.minecraft.potion.Potion;

/**
 * Created by Nictogen on 1/21/19.
 */
public class GenePotionPunch extends Gene
{
	public GenePotionPunch(String displayName, int potionID, int maxDuration, int maxAmplifier)
	{
		super(AbilityPotionPunch.class, displayName, true);
		Potion potion = Potion.getPotionById(potionID);
		//noinspection ConstantConditions
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
		this.addDataMod(new Gene.DataMod<>(AbilityPotionPunch.POTION, potion, false));
		this.addDataMod(new Gene.DataMod<>(AbilityPotionPunch.DURATION, maxDuration));
		this.addDataMod(new Gene.DataMod<>(AbilityPotionPunch.AMPLIFIER, maxAmplifier));
	}

}
