package com.nic.tfw.superpower.genes;

import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityFlight;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.passive.EntityTameable;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Nictogen on 1/23/19.
 */
public class GeneFlight extends Gene
{

	private float maxSpeed;

	public GeneFlight(Class<? extends AbilityFlight> c, String displayName, float maxSpeed)
	{
		super(c, displayName);
		this.maxSpeed = maxSpeed;
	}

	@Override float getQuality(Ability ability, Random r)
	{
		AbilityFlight a = (AbilityFlight) ability;
		return ((float) a.speed / this.maxSpeed);
	}

	@Override public Ability createAbilityInstance(EntityLivingBase entity, GeneSet.GeneData geneData)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
	{
		if (entity instanceof EntityCreature)
		{
			if (((EntityCreature) entity).tasks.taskEntries.stream()
					.noneMatch(entityAITaskEntry -> entityAITaskEntry.action instanceof EntityAIWanderAvoidWaterFlying))
			{
				((EntityCreature) entity).tasks.addTask(3, new EntityAIWanderAvoidWaterFlying((EntityCreature) entity, 1.0D));
			}
			if (entity instanceof EntityTameable)
			{
				((EntityCreature) entity).tasks.addTask(3, new EntityAIFollowOwnerFlying((EntityTameable) entity, 1.0D, 5.0f, 1.0f));
			}
		}

		return ability.getAbilityClass().getConstructor(EntityLivingBase.class, double.class, double.class, boolean.class).newInstance(entity, maxSpeed*geneData.quality, maxSpeed*geneData.quality*2.5, true);
	}
}
