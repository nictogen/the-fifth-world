package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityItemCreation;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneItemCreation extends Gene
{
	public GeneItemCreation(String displayName, int maxCooldown, ItemStack item)
	{
		super(AbilityItemCreation.class, displayName, true);
		setRegistryName(ability.getRegistryName() + "_" + item.getItem().getRegistryName().toString().replace(":", "_"));
		addDataMod(new DataMod<>(AbilityItemCreation.COOLDOWN, maxCooldown));
		addDataMod(new DataMod<>(AbilityItemCreation.ITEM, item));
	}
}
