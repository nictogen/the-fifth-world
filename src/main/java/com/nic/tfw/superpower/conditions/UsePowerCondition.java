package com.nic.tfw.superpower.conditions;

import com.nic.tfw.superpower.abilities.IDefect;
import lucraft.mods.lucraftcore.superpowers.SuperpowerEntityHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.events.AbilityKeyEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 1/17/19.
 */
@Mod.EventBusSubscriber
public class UsePowerCondition extends OneUseCondition
{
	@SubscribeEvent
	public static void onKey(AbilityKeyEvent.Server event) {
		SuperpowerEntityHandler handler = SuperpowerHandler.getSuperpowerEntityHandler(event.entity);
		if(handler == null || !handler.getAbilities().contains(event.ability)) return;
		for (Ability ability : handler.getAbilities())
		{
			if(ability instanceof IDefect){
				if(((IDefect) ability).getCondition() instanceof UsePowerCondition){
					((UsePowerCondition) ((IDefect) ability).getCondition()).setEnabled(event.entity);
				}
			}
		}
	}
}
