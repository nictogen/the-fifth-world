package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.potion.Potion;

import java.util.Optional;

/**
 * Created by Nictogen on 1/21/19.
 */
public class GenePotionPunch extends Gene
{
	public GenePotionPunch(Class<? extends Ability> c, int[] fields, Object[] maxValues, String displayName, Potion potion)
	{
		Optional<Ability.AbilityEntry> abilityEntry = Ability.ABILITY_REGISTRY.getValuesCollection().stream().filter(
				abilityEntry1 -> abilityEntry1.getAbilityClass() == c).findFirst();
		this.ability = abilityEntry.get();
		this.fields = fields;
		this.maxValues = maxValues;
		this.displayName = displayName;
		setRegistryName(ability.getRegistryName() + "_" + potion.getRegistryName().toString().replace(':', '_'));
	}
}
