package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.heroesexpansion.potions.PotionKryptonitePoison;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.karma.potions.PotionKnockOut;
import lucraft.mods.lucraftcore.superpowers.events.LivingSuperpowerEvent;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 2019-03-09.
 */
@Mod.EventBusSubscriber
public class ItemSafetyGoggles extends ItemArmor
{
	public static ResourceLocation TEXTURE = new ResourceLocation(TheFifthWorld.MODID, "textures/model/armor/safety_goggles.png");

	public ItemSafetyGoggles()
	{
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
		setTranslationKey("safety_goggles");
		setRegistryName(TheFifthWorld.MODID, "safety_goggles");
		setCreativeTab(LucraftCore.CREATIVE_TAB);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return TEXTURE.toString();
	}

	@SubscribeEvent
	public static void onPotionApplicable(PotionEvent.PotionApplicableEvent event){
		if(event.getPotionEffect().getPotion() instanceof PotionKnockOut || event.getPotionEffect().getPotion() instanceof PotionKryptonitePoison) return;
		if(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == TheFifthWorld.Items.lab_coat && event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == TheFifthWorld.Items.safety_goggles){
			event.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public static void onGetBlindness(LivingSuperpowerEvent event){
		if(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == TheFifthWorld.Items.safety_goggles)
			event.setCanceled(true);

	}
}
