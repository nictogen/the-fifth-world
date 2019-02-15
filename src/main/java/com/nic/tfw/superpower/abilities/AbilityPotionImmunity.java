package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityPotionImmunity extends AbilityConstant
{
	private Potion potion;

	public AbilityPotionImmunity(EntityLivingBase player, Potion potion)
	{
		super(player);
		this.potion = potion;
	}

	@Override public void updateTick()
	{
		if(this.entity.isPotionActive(this.potion)){
			this.entity.removePotionEffect(this.potion);
		}
	}
}
