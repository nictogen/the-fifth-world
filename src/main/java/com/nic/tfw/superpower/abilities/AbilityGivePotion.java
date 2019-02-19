package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityToggle;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataPotion;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by Nictogen on 2019-02-18.
 */
public class AbilityGivePotion extends AbilityToggle
{
	public static AbilityData<Potion> POTION = new AbilityDataPotion("potion").disableSaving().setSyncType(EnumSync.SELF).enableSetting("potion", "Sets the potion to give to yourself.");
	public static AbilityData<Integer> AMPLIFIER = new AbilityDataInteger("amplifier").disableSaving().setSyncType(EnumSync.NONE).enableSetting("amplifier", "Sets the amplifier for the potion effect");

	public AbilityGivePotion(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override
	public void registerData() {
		super.registerData();
		this.dataManager.register(POTION, MobEffects.HEALTH_BOOST);
		this.dataManager.register(AMPLIFIER, 0);
	}

	@Override public void updateTick()
	{
		entity.addPotionEffect(new PotionEffect(getDataManager().get(POTION), 1, getDataManager().get(AMPLIFIER)));
	}
}
