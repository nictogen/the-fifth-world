package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.LucraftCore;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Nictogen on 2019-03-09.
 */
public class ItemLabCoat extends ItemArmor
{
	public static ResourceLocation TEXTURE = new ResourceLocation(TheFifthWorld.MODID, "textures/model/armor/labcoat.png");

	public ItemLabCoat()
	{
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST);
		setTranslationKey("lab_coat");
		setRegistryName(TheFifthWorld.MODID, "lab_coat");
		setCreativeTab(LucraftCore.CREATIVE_TAB);
	}
	@SideOnly(Side.CLIENT)
	@Nullable @Override public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelBipedAll.MODEL;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return TEXTURE.toString();
	}

	@SideOnly(Side.CLIENT)
	public static class ModelBipedAll extends ModelBiped {

		public static ModelBipedAll MODEL = new ModelBipedAll();

		public ModelBipedAll()
		{
			super(0.4f);
		}

		@Override public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			this.setVisible(true);
			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}


}
