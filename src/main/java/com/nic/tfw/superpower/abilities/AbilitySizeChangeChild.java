package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilitySizeChange;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 2019-03-13.
 */
public class AbilitySizeChangeChild extends AbilitySizeChange
{
	public AbilitySizeChangeChild(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().set(Ability.SHOW_IN_BAR, false);
	}

	@Override public void firstTick()
	{
		if (!entity.hasCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null))
			return;

		ISizeChanging data = entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
		float f = this.getSize();

		if (this.getSizeChanger() == null)
			data.setSize(f);
		else
			data.setSize(f, this.getSizeChanger());
	}

	@Override public void lastTick()
	{
		if (!entity.hasCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null))
			return;

		ISizeChanging data = entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
		float f = 1F;

		if (this.getSizeChanger() == null)
			data.setSize(f);
		else
			data.setSize(f, this.getSizeChanger());

	}

	public boolean isEnabled() {
		return parentAbility != null && parentAbility.isEnabled();
	}

}
