package com.nic.tfw.superpower.abilities;

import com.nic.tfw.TheFifthWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nictogen on 2019-03-12.
 */
public class AbilityIronSkin extends AbilitySkinChange
{
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
	}

	@SideOnly(Side.CLIENT)
	@Override public ResourceLocation getTexture()
	{
		if(!(entity instanceof AbstractClientPlayer))return super.getTexture();
		String s = getDataManager().get(SKIN);

		getDataManager().set(AbilitySkinChange.SMALL_ARMS, ((AbstractClientPlayer) entity).getSkinType().equals("slim"));

		for (ResourceLocation hashedTexture : Handler.hashedTextures.keySet())
			if (hashedTexture.toString().equals(s))
				return super.getTexture();

		BufferedImage image = Handler.getPlayerSkin((AbstractClientPlayer) entity);
		if(image != null)
		{
			image = createIronTexture(image);
			ResourceLocation rL = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(TheFifthWorld.MODID + ":iron_skin/" + entity.getUniqueID(), new DynamicTexture(image));
			Handler.hashedTextures.put(rL, image);
		}

		return super.getTexture();
	}

	public BufferedImage createIronTexture(BufferedImage originalImage)
	{
		BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
			{
				if(originalImage.getRGB(x, y) != 0)
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
}
