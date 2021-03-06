package com.nic.tfw.proxy;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.superpower.abilities.defects.ApplyShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Nictogen on 1/10/19.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{

	@Override public void onPreInit(FMLPreInitializationEvent event)
	{
		super.onPreInit(event);
	}

	@Override public void onInit(FMLInitializationEvent event)
	{
		super.onInit(event);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemVial.GeneColor(), TheFifthWorld.Items.glass_vial);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemVial.GeneColor(), TheFifthWorld.Items.injection_gun);
	}

	@SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(TheFifthWorld.Items.glass_vial, 0, new ModelResourceLocation(TheFifthWorld.Items.glass_vial.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(TheFifthWorld.Items.injection_gun, 0, new ModelResourceLocation(TheFifthWorld.Items.injection_gun.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(TheFifthWorld.Items.lab_coat, 0, new ModelResourceLocation(TheFifthWorld.Items.lab_coat.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(TheFifthWorld.Items.safety_goggles, 0, new ModelResourceLocation(TheFifthWorld.Items.safety_goggles.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TheFifthWorld.Blocks.centrifuge), 0, new ModelResourceLocation(TheFifthWorld.Blocks.centrifuge.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TheFifthWorld.Blocks.microscope), 0, new ModelResourceLocation(TheFifthWorld.Blocks.microscope.getRegistryName(), "inventory"));

	}

	public void stopSounds(EntityPlayer player){
		if(player == null || Minecraft.getMinecraft().player == player)
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
	}

	public void loadShader(String shader){
		ApplyShader.Handler.shader = shader;
	}
}
