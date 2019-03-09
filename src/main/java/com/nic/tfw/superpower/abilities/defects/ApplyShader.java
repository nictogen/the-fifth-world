package com.nic.tfw.superpower.abilities.defects;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataString;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Nictogen on 2019-03-09.
 */
public class ApplyShader extends AbilityConstant
{
	public static final AbilityData<String> SHADER = new AbilityDataString("shader").disableSaving().setSyncType(EnumSync.SELF).enableSetting("shader", "Sets the shader to load, in resourcelocation format i.e.: \"minecraft:shaders/post/shader.json\"");

	public ApplyShader(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().register(SHADER, "");
	}

	@Override public void updateTick()
	{
		TheFifthWorld.proxy.loadShader(getDataManager().get(SHADER));
	}

	@Override public void lastTick()
	{
		TheFifthWorld.proxy.loadShader("");
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber
	public static class Handler
	{
		public static String shader = "";
		public static boolean applyingShader = false;

		@SubscribeEvent
		public static void onTick(TickEvent.ClientTickEvent e)
		{
			Minecraft mc = Minecraft.getMinecraft();
			if (mc == null || mc.player == null || e.phase == TickEvent.Phase.START || mc.isGamePaused())
				return;

			if(!applyingShader && !shader.equals("")){
				applyingShader = true;
				mc.entityRenderer.loadShader(new ResourceLocation(shader));
			} else if(shader.equals("") && applyingShader){
				mc.entityRenderer.stopUseShader();
			} else if(!shader.equals("") && mc.entityRenderer != null && (mc.entityRenderer.getShaderGroup() == null || !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals(shader))){
				mc.entityRenderer.loadShader(new ResourceLocation(shader));
			}
		}
	}
}
