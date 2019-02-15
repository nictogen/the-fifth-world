package com.nic.tfw.superpower.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

/**
 * Created by Nictogen on 1/22/19.
 */
//TODO brainwashing (basically choose tasks and target tasks)
public class AbilityMakeHostile extends AbilityConstant
{
	public AbilityMakeHostile(EntityLivingBase player)
	{
		super(player);
	}

	@Override public void updateTick()
	{
		if (entity instanceof EntityLiving)
		{
			if (((EntityLiving) entity).tasks.taskEntries.stream().noneMatch(entityAITaskEntry -> entityAITaskEntry.action instanceof EntityAIAttackMelee))
			{
				for (EntityAITasks.EntityAITaskEntry ai : ((EntityLiving) entity).tasks.taskEntries.stream().collect(Collectors.toList()))
				{
					if (ai.priority != 0)
						((EntityLiving) entity).tasks.taskEntries.remove(ai);
					((EntityLiving) entity).tasks.addTask(ai.priority + 2, ai.action);
				}
				((EntityLiving) entity).tasks.addTask(1, new EntityAILeapAtTarget(((EntityLiving) entity), 0.4F));
				((EntityLiving) entity).tasks.addTask(2, new EntityAIAttackMelee(((EntityCreature) entity), 1.0D, true));
				((EntityLiving) entity).targetTasks.addTask(1, new EntityAIHurtByTarget(((EntityCreature) entity), true, EntityPigZombie.class));
				((EntityLiving) entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget(((EntityCreature) entity), EntityLivingBase.class, true));
			} else {
				for (EntityAITasks.EntityAITaskEntry entityAITaskEntry : ((EntityLiving) entity).tasks.taskEntries.stream()
						.filter(entityAITaskEntry -> entityAITaskEntry.action instanceof EntityAIAttackMelee).collect(Collectors.toList()))
				{
					try
					{
						checkAndPerformAttack((EntityAIAttackMelee) entityAITaskEntry.action);
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void checkAndPerformAttack(EntityAIAttackMelee ai) throws IllegalAccessException
	{
		EntityLivingBase target = ((EntityLiving) entity).getAttackTarget();
		if(target != null)
		{
			double distToEnemySqr = entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
			double d0 = (double)(entity.width * 2.0F * entity.width * 2.0F + target.width);

			Field attackTick = EntityAIAttackMelee.class.getDeclaredFields()[2];
			attackTick.setAccessible(true);

			if (distToEnemySqr <= d0 && ((int) attackTick.get(ai)) == 20)
			{
				entity.swingArm(EnumHand.MAIN_HAND);
				double amount = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null ? entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() : 1.0;
				target.attackEntityFrom(DamageSource.causeMobDamage(entity), (float) amount);
			}
		}
	}
}
