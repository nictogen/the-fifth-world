package com.nic.tfw.superpower.abilities;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.util.LimbManipulationUtil;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityConstant;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityHeld;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityToggle;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataBoolean;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataColor;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataString;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import lucraft.mods.lucraftcore.util.events.RenderModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Nictogen on 2019-03-10.
 */
public class AbilitySkinChange extends AbilityConstant
{
	public static final AbilityData<String> SKIN = new AbilityDataString("skin").setSyncType(EnumSync.EVERYONE)
			.enableSetting("skin", "The skin to change in to.");

	public static final AbilityData<Boolean> SMALL_ARMS = new AbilityDataBoolean("small_arms").setSyncType(EnumSync.EVERYONE)
			.enableSetting("small_arms", "If the skin to change in to has small arms or not");

	public static final AbilityData<Color> MAIN_COLOR = new AbilityDataColor("main_color").setSyncType(EnumSync.EVERYONE)
			.enableSetting("main_color", "The main transition color from default skin to the one changing in to.");

	public static final AbilityData<Color> SECONDARY_COLOR = new AbilityDataColor("secondary_color").setSyncType(EnumSync.EVERYONE)
			.enableSetting("secondary_color", "The secondary color that the main one will be randomly offset by.");

	public AbilitySkinChange(EntityLivingBase entity)
	{
		super(entity);
	}

	@Override public void registerData()
	{
		super.registerData();
		getDataManager().register(SKIN, "");
		getDataManager().register(SMALL_ARMS, false);
		getDataManager().register(MAIN_COLOR, Color.BLACK);
		getDataManager().register(SECONDARY_COLOR, Color.BLACK);
	}

	@Override public void updateTick()
	{
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		String s = getDataManager().get(SKIN);
		ResourceLocation r = null;
		for (ResourceLocation hashedTexture : Handler.hashedTextures.keySet())
			if (hashedTexture.toString().equals(s))
				r = hashedTexture;
		if (r == null)
		{
			r = new ResourceLocation(s);
			try
			{
				Handler.hashedTextures.put(r, TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(r).getInputStream()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (parentAbility instanceof AbilityToggle || parentAbility instanceof AbilityHeld)
		{
			BufferedImage oldImage = Handler.getPlayerSkin((AbstractClientPlayer) entity);
			BufferedImage bufferedImage = Handler.hashedTextures.get(r);
			if(bufferedImage != null && oldImage != null)
			{
				ResourceLocation r2 = Handler.animateTexture(bufferedImage, oldImage, Minecraft.getMinecraft().renderEngine, parentAbility.getTicks(), 20, getDataManager().get(SMALL_ARMS), getDataManager().get(MAIN_COLOR), getDataManager().get(SECONDARY_COLOR));
				if(r2 != null)
				{
					r = r2;
					Handler.toDelete = r;
				}
			}
		}
		return r;
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class Handler
	{
		public static final HashMap<ResourceLocation, BufferedImage> hashedTextures = new HashMap<>();
		public static ResourceLocation toDelete = null;

		public static BufferedImage getPlayerSkin(AbstractClientPlayer player)
		{
			try
			{
				ThreadDownloadImageData t = (ThreadDownloadImageData) Minecraft.getMinecraft().getTextureManager().getTexture(player.getLocationSkin());
				Field f = ThreadDownloadImageData.class.getDeclaredFields()[5];
				f.setAccessible(true);
				return (BufferedImage) f.get(t);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		@SubscribeEvent
		public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
		{
			if (!(event.getEntity() instanceof EntityPlayer))
				return;

			for (Ability ability : Ability.getAbilities(event.getEntityLiving()))
			{
				if (ability instanceof AbilitySkinChange && ability.isUnlocked() && !ability.getDataManager().get(SKIN).equals(""))
				{
					RenderPlayer useRender = event.getRenderer().getRenderManager().getSkinMap()
							.get(ability.getDataManager().get(SMALL_ARMS) ? "slim" : "default");
					if (useRender != event.getRenderer())
					{
						ModelPlayer model = event.getRenderer().getMainModel();
						LimbManipulationUtil.getLimbManipulator(model, LimbManipulationUtil.Limb.LEFT_ARM, useRender.getMainModel());
						LimbManipulationUtil.getLimbManipulator(model, LimbManipulationUtil.Limb.RIGHT_ARM, useRender.getMainModel());
					}
					return;
				}
			}

		}

		@SubscribeEvent
		public static void onSetRotationAngles(RenderModelEvent.SetRotationAngels event)
		{
			if (event.type != RenderModelEvent.ModelSetRotationAnglesEventType.PRE || !(event.getEntity() instanceof AbstractClientPlayer))
				return;
			Render r = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(event.getEntity());
			if (!(r instanceof RenderLivingBase) || event.model != ((RenderLivingBase) r).getMainModel())
				return;

			for (Ability ability : Ability.getAbilities((EntityLivingBase) event.getEntity()))
			{
				if (ability instanceof AbilitySkinChange && ability.isUnlocked() && !ability.getDataManager().get(SKIN).equals(""))
				{
					ResourceLocation rL = ((AbilitySkinChange) ability).getTexture();
					if (rL != null)
						Minecraft.getMinecraft().renderEngine.bindTexture(rL);
					return;
				}
			}
		}

		@SubscribeEvent
		public static void onRenderPlayerPost(RenderPlayerEvent.Post event)
		{
			if (toDelete != null)
			{
				event.getRenderer().getRenderManager().renderEngine.deleteTexture(toDelete);
				toDelete = null;
			}
		}

		private static ResourceLocation animateTexture(BufferedImage originalImage, BufferedImage oldImage, TextureManager manager, int timer,
				int maxTimer,
				boolean smallArms, Color color1, Color color2)
		{
			if (timer == maxTimer)
			{
				return null;
			}

			//Clone
			BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
			Graphics g = image.createGraphics();
			g.drawImage(originalImage, 0, 0, null);
			g.dispose();

			// Helmet
			float helmetProgress = (float) MathHelper.clamp(timer, 0, maxTimer / 2) / (maxTimer / 2F);
			animY(image, oldImage, 0, 17, 64, 8, helmetProgress, helmetProgress != 1F, color1, color2);
			animY(image, oldImage, 8, 0, 16, 8, helmetProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 40, 0, 48, 8, helmetProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 16, 0, 24, 8, helmetProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 48, 0, 56, 8, helmetProgress > 0F ? 1F : 0F, false, color1, color2);

			// Chest
			float chestProgress = (float) MathHelper.clamp(timer, 0, maxTimer / 2) / (maxTimer / 2F);
			animY(image, oldImage, 16, 20, 40, 32, chestProgress, color1, color2);
			animY(image, oldImage, 16, 36, 40, 48, chestProgress, color1, color2);
			animY(image, oldImage, 28, 16, 36, 20, chestProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 28, 32, 36, 36, chestProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 20, 16, 28, 20, chestProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 20, 32, 28, 36, chestProgress > 0F ? 1F : 0F, false, color1, color2);

			// Arms
			animY(image, oldImage, 40, 20, 56, 32, chestProgress, color1, color2);
			animY(image, oldImage, 40, 36, 56, 48, chestProgress, color1, color2);
			animY(image, oldImage, 32, 52, 48, 64, chestProgress, color1, color2);
			animY(image, oldImage, 48, 52, 64, 64, chestProgress, color1, color2);
			animY(image, oldImage, 44, 16, smallArms ? 47 : 48, 20, chestProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 44, 32, smallArms ? 47 : 48, 36, chestProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, smallArms ? 47 : 48, 16, 52, 20, chestProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, smallArms ? 47 : 48, 32, 52, 36, chestProgress == 1F ? 1F : 0F, false, color1, color2);

			animY(image, oldImage, 36, 48, smallArms ? 39 : 40, 52, chestProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 52, 48, smallArms ? 55 : 56, 52, chestProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, smallArms ? 39 : 40, 48, 44, 52, chestProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, smallArms ? 55 : 56, 48, 60, 52, chestProgress == 1F ? 1F : 0F, false, color1, color2);

			// Legs
			float legsProgress = (float) MathHelper.clamp(timer - (maxTimer / 2), 0, maxTimer / 2) / (maxTimer / 2F);
			animY(image, oldImage, 0, 20, 16, 32, legsProgress, legsProgress != 0F, color1, color2);
			animY(image, oldImage, 0, 36, 16, 48, legsProgress, legsProgress != 0F, color1, color2);
			animY(image, oldImage, 0, 52, 16, 64, legsProgress, legsProgress != 0F, color1, color2);
			animY(image, oldImage, 16, 52, 32, 64, legsProgress, legsProgress != 0F, color1, color2);
			animY(image, oldImage, 4, 16, 8, 20, legsProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 4, 32, 8, 36, legsProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 8, 16, 12, 20, legsProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 8, 32, 12, 36, legsProgress == 1F ? 1F : 0F, false, color1, color2);

			animY(image, oldImage, 20, 48, 24, 52, legsProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 4, 48, 8, 52, legsProgress > 0F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 24, 48, 28, 52, legsProgress == 1F ? 1F : 0F, false, color1, color2);
			animY(image, oldImage, 8, 48, 16, 52, legsProgress == 1F ? 1F : 0F, false, color1, color2);

			return manager.getDynamicTextureLocation(TheFifthWorld.MODID + ":animated_texture", new DynamicTexture(image));
		}

	}

	private static void animY(BufferedImage image, BufferedImage oldImage, int startX, int startY, int endX, int endY, float progress, Color color1,
			Color color2)
	{
		animY(image, oldImage, startX, startY, endX, endY, progress, true, color1, color2);
	}

	private static void animY(BufferedImage image, BufferedImage oldImage, int startX, int startY, int endX, int endY, float progress, boolean trans,
			Color color1, Color color2)
	{
		Random rand = new Random();
		for (int x = Math.min(startX, endX); x < Math.max(startX, endX); x++)
		{
			for (int y = Math.min(startY, endY); y < Math.max(startY, endY); y++)
			{
				if (!(startY < endY) == (y < startY + progress * (endY - startY)))
				{
					image.setRGB(x, y, oldImage.getRGB(x, y));
				}
				if (trans && y == (int) (startY + progress * (endY - startY)))
				{
					float randomFloat = rand.nextFloat();
					int rDiff = (int) -((color1.getRed() - color2.getRed()) * randomFloat);
					int gDiff = (int) -((color1.getGreen() - color2.getGreen()) * randomFloat);
					int bDiff = (int) -((color1.getBlue() - color2.getBlue()) * randomFloat);

					Color c = new Color(Math.min(255, Math.max(0, color1.getRed() + rDiff)), Math.min(255, Math.max(0, color1.getGreen() + gDiff)), Math.min(255, Math.max(0, color1.getBlue() + bDiff)));
					image.setRGB(x, y, c.getRGB());
				}
			}
		}
	}

}
