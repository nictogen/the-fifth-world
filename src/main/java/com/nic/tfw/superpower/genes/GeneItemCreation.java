package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityItemCreation;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class GeneItemCreation extends Gene
{
	private int maxCooldown;
	private ItemStack stack;

	public GeneItemCreation(String displayName, int maxCooldown, ItemStack item)
	{
		super(AbilityItemCreation.class, displayName, true);
		this.maxCooldown = maxCooldown;
		this.stack = item;
		setRegistryName(ability.getRegistryName() + "_" + stack.getItem().getRegistryName().toString().replace(":", "_"));
	}

	@Override float getQuality(Ability ability, Random r)
	{
		return 1f - ((float) ability.getCooldown() / (float) maxCooldown);
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
	{
		return new AbilityItemCreation(entity, stack, (int) ((1f - geneData.quality)*maxCooldown));
	}
}
