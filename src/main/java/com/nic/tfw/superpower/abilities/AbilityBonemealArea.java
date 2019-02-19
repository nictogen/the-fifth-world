package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class AbilityBonemealArea extends AbilityAction
{
	public static final AbilityData<Integer> RADIUS = new AbilityDataInteger("radius").disableSaving().setSyncType(EnumSync.SELF).enableSetting("radius", "The radius of the bonemeal effect.");

	public AbilityBonemealArea(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public boolean action()
	{
		for (int x = -getDataManager().get(RADIUS); x < getDataManager().get(RADIUS); x++)
			for (int z = -getDataManager().get(RADIUS); z < getDataManager().get(RADIUS); z++)
				for (int y = -1; y < 1; y++)
					ItemDye.applyBonemeal(ItemStack.EMPTY, entity.world, entity.getPosition().add(x, y, z));
		return true;
	}
}
