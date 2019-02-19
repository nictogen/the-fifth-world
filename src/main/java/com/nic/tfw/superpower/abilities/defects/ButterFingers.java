package com.nic.tfw.superpower.abilities.defects;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Nictogen on 1/17/19.
 */
public class ButterFingers extends AbilityConstant
{
	public ButterFingers(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if (!entity.getHeldItemMainhand().isEmpty())
		{
			ItemStack stack = entity.getHeldItemMainhand();
			ItemStack stack1 = stack.copy();
			stack1.setCount(1);
			stack.shrink(1);
			EntityItem entityItem = entity.entityDropItem(stack1, 0f);
			if (entityItem != null)
			{
				entityItem.setPickupDelay(40);
				float f = entity.getRNG().nextFloat() * 0.5F;
				float f1 = entity.getRNG().nextFloat() * ((float) Math.PI * 2F);
				entityItem.motionX = (double) (-MathHelper.sin(f1) * f);
				entityItem.motionZ = (double) (MathHelper.cos(f1) * f);
				entityItem.motionY = 0.20000000298023224D;
			}
		}
	}

}

