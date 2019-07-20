package com.nic.tfw.superpower.abilities;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataInteger;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Nictogen on 2019-03-12.
 */
public class AbilityIronSkin extends AbilitySkinChange
{
	public static final AbilityData<Integer> HEAT = new AbilityDataInteger("heat").setSyncType(EnumSync.EVERYONE);

	public AbilityIronSkin(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().set(AbilitySkinChange.SKIN, "dynamic/" + TheFifthWorld.MODID + ":iron_skin/" + (entity == null ? "" : entity.getUniqueID() + "_1"));
		getDataManager().set(AbilitySkinChange.MAIN_COLOR, Color.LIGHT_GRAY);
		getDataManager().set(AbilitySkinChange.SECONDARY_COLOR, Color.GRAY);
		getDataManager().register(HEAT, 0);
	}

	@Override public void updateTick()
	{
		if(this.entity.world.isRemote) return;
		int heat = getDataManager().get(HEAT);
		if (entity.isBurning())
		{
			try
			{
				setBurning(entity, false);
			}
			catch (InvocationTargetException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
			getDataManager().set(HEAT, heat + 1);
		}
		else if (entity.ticksExisted % 5 == 0 && heat > 0)
			getDataManager().set(HEAT, heat - 1);
	}

	@SideOnly(Side.CLIENT)
	@Override public ResourceLocation getTexture()
	{
		if (!(entity instanceof AbstractClientPlayer))
			return super.getTexture();
		String s = getDataManager().get(SKIN);

		getDataManager().set(AbilitySkinChange.SMALL_ARMS, ((AbstractClientPlayer) entity).getSkinType().equals("slim"));

		for (ResourceLocation hashedTexture : AbilitySkinChange.Handler.hashedTextures.keySet())
			if (hashedTexture.toString().equals(s))
				return super.getTexture();

		BufferedImage image = AbilitySkinChange.Handler.getPlayerSkin((AbstractClientPlayer) entity);
		if (image != null)
		{
			image = createIronTexture(image);
			ResourceLocation rL = Minecraft.getMinecraft().renderEngine
					.getDynamicTextureLocation(TheFifthWorld.MODID + ":iron_skin/" + entity.getUniqueID(), new DynamicTexture(image));
			AbilitySkinChange.Handler.hashedTextures.put(rL, image);
		}

		return super.getTexture();
	}

	public BufferedImage createIronTexture(BufferedImage originalImage)
	{
		BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
			{
				if (originalImage.getRGB(x, y) != 0)
				{
					Color c = new Color(originalImage.getRGB(x, y));
					int gray = (int) (Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), new float[3])[2] * 255f);
					gray += y % 2 == 0 ? 15 : 0;
					gray = Math.min(255, gray);
					image.setRGB(x, y, new Color(gray, gray, gray, c.getAlpha()).getRGB());
				}
			}
		return image;
	}

	public static void setBurning(EntityLivingBase entityLivingBase, boolean burning) throws InvocationTargetException, IllegalAccessException
	{
		//	Method m = ObfuscationReflectionHelper.findMethod(Entity.class, "func_70052_a", Void.class, int.class, boolean.class); //TODO switch before release and test
		Method m = ObfuscationReflectionHelper.findMethod(Entity.class, "setFlag", Void.class, int.class, boolean.class);
		m.setAccessible(true);
		m.invoke(entityLivingBase, 0, burning);
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class Handler
	{
		private static boolean burning = false;

		@SubscribeEvent
		public static void onRenderLivingPre(RenderLivingEvent.Pre event) throws InvocationTargetException, IllegalAccessException
		{
			if (event.getEntity().isBurning())
				for (Ability ability : Ability.getAbilities(event.getEntity()))
				{
					if (ability instanceof AbilityIronSkin && ability.isUnlocked())
					{
						setBurning(event.getEntity(), false);
						event.getEntity().extinguish();
						burning = true;
						return;
					}
				}

		}

		@SubscribeEvent(receiveCanceled = true)
		public static void onRenderLivingPost(RenderLivingEvent.Post event) throws InvocationTargetException, IllegalAccessException
		{
//			if (burning)
//			{
//				setBurning(event.getEntity(), true);
//				burning = false;
//			}

		}


	}
}
