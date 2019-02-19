package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityGivePotion;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityPotionPunch;
import net.minecraft.potion.Potion;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class GeneGivePotion extends Gene
{
	public GeneGivePotion(String displayName, int potionID, int maxAmplifier)
	{
		super(AbilityGivePotion.class, displayName,true);
		Potion potion = Potion.getPotionById(potionID);
		//noinspection ConstantConditions
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
		this.addDataMod(new Gene.DataMod<>(AbilityPotionPunch.POTION, potion, false));
		this.addDataMod(new Gene.DataMod<>(AbilityPotionPunch.AMPLIFIER, maxAmplifier));
	}
}
