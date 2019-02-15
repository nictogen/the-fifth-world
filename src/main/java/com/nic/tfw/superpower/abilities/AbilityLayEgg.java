package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Created by Nictogen on 2019-02-14.
 */
public class AbilityLayEgg extends AbilityAction
{
	public AbilityLayEgg(EntityLivingBase player, int cd)
	{
		super(player);
		this.setMaxCooldown(cd);
	}

	@Override public boolean action()
	{
		entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (entity.getRNG().nextFloat() * 0.4F + 0.8F));

		if (!entity.world.isRemote)
		{
			EntityEgg entityegg = new EntityEgg(entity.world, entity);
			entityegg.shoot(entity, 90.0f, entity.rotationYaw, 0.0F, 1.5F, 0.0F);
			entity.world.spawnEntity(entityegg);
		}
		return true;
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Items.EGG), x, y);
	}
}
