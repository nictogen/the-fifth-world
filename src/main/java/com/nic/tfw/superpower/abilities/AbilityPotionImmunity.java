package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataPotion;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityPotionImmunity extends AbilityConstant
{
	public static AbilityData<Potion> POTION = new AbilityDataPotion("potion").disableSaving().setSyncType(EnumSync.SELF).enableSetting("potion", "Sets the potion to give immunity to");

	public AbilityPotionImmunity(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if(this.entity.isPotionActive(this.dataManager.get(POTION))){
			this.entity.removePotionEffect(this.dataManager.get(POTION));
		}
	}

	@Override
	public void registerData() {
		super.registerData();
		this.dataManager.register(POTION, MobEffects.POISON);
	}
}
