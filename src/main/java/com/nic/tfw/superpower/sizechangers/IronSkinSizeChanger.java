package com.nic.tfw.superpower.sizechangers;

import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.sizechanging.sizechanger.SizeChanger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 2019-03-13.
 */
@Mod.EventBusSubscriber
public class IronSkinSizeChanger extends SizeChanger
{

	public static final IronSkinSizeChanger IRON_SKIN_SIZE_CHANGER = new IronSkinSizeChanger();

	@Override
	public void onSizeChanged(EntityLivingBase entity, ISizeChanging data, float size) { }

	@Override
	public int getSizeChangingTime(EntityLivingBase entity, ISizeChanging data, float estimatedSize) {
		return 20;
	}

	@Override
	public void onUpdate(EntityLivingBase entity, ISizeChanging data, float size) { }

	@Override
	public boolean start(EntityLivingBase entity, ISizeChanging data, float size, float estimatedSize) {
		return true;
	}

	@Override
	public void end(EntityLivingBase entity, ISizeChanging data, float size) { }

	@SubscribeEvent
	public static void onRegisterSizeChangers(RegistryEvent.Register<SizeChanger> e) {
//		e.getRegistry().register(IRON_SKIN_SIZE_CHANGER.setRegistryName("iron_skin"));
	}
}
