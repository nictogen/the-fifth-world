package com.nic.tfw.superpower.abilities;

import com.nic.tfw.util.ClientUtils;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityHeld;
import lucraft.mods.lucraftcore.superpowers.render.RenderSuperpowerLayerEvent;
import lucraft.mods.lucraftcore.util.helper.LCEntityHelper;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

/**
 * Created by Nictogen on 1/24/19.
 */
public class AbilityScreech extends AbilityHeld
{
	public AbilityScreech(EntityLivingBase player)
	{
		super(player);
	}

	public AbilityScreech(EntityLivingBase player, float damage)
	{
		super(player);
		this.damage = damage;
	}

	public float damage = 1f;

	@Override public void updateTick()
	{
		for (Entity entity1 : entity.world
				.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(Math.min(ticks, 10)), input -> LCEntityHelper.isInFrontOfEntity(entity, input)))
		{
			float speed = 1;
			entity1.motionY += -Math.sin((double)(entity.rotationPitch * (float)Math.PI / 180.0F)) * speed;
			entity1.motionX += -Math.sin((double) (entity.rotationYaw * (float) Math.PI / 180.0F)) * speed;
			entity1.motionZ += Math.cos((double) (entity.rotationYaw * (float) Math.PI / 180.0F)) * speed;

			if(entity.getDistance(entity1) < 2f || entity1.collidedHorizontally){
				entity1.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
			}
		}

//		if(entity instanceof EntityPlayer)
//		{
//			RayTraceResult result = PlayerHelper.rayTrace((EntityPlayer) entity, 3);
//			if(result.typeOfHit == RayTraceResult.Type.BLOCK){
//				entity.motionY += Math.sin((double)(entity.rotationPitch * (float)Math.PI / 180.0F))/10;
//				entity.motionX += Math.sin((double) (entity.rotationYaw * (float) Math.PI / 180.0F))/20;
//				entity.motionZ += Math.cos((double) (entity.rotationYaw * (float) Math.PI / 180.0F))/20;
//			}
//		}
	}

	@Override public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Items.RECORD_11), x, y);
	}

	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class Renderer
	{
		@SubscribeEvent
		public static void onRenderWorldLast(RenderWorldLastEvent event)
		{
			if (Minecraft.getMinecraft().player == null)
				return;

			EntityPlayer player = Minecraft.getMinecraft().player;
			AxisAlignedBB voxel = new AxisAlignedBB(-0.025, -0.025, -0.025, 0.025, 0.025, 0.025);

			for (AbilityScreech ab : Ability.getAbilitiesFromClass(Ability.getCurrentAbilities(player), AbilityScreech.class))
			{
				if (ab != null && ab.isUnlocked() && ab.isEnabled())
				{
					LCRenderHelper.setLightmapTextureCoords(240, 240);
					GlStateManager.pushMatrix();
					GlStateManager.disableLighting();
					GlStateManager.disableTexture2D();
					GlStateManager.disableCull();
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();

					GlStateManager.translate(0, player.getEyeHeight(), 0);
					GlStateManager.rotate(-player.rotationYaw, 0, 1, 0);
					GlStateManager.rotate(player.rotationPitch, 1, 0, 0);

					GlStateManager.translate(0, 0, 0.75);


					for (int i = 0; i < 7 && i < ab.ticks; i++)
					{
						float offset = ((float) i) + ((ab.ticks % 4 + Minecraft.getMinecraft().getRenderPartialTicks()) / 5f);
						for (int angle = 0; angle < 360; angle += 8 - i)
						{
							GlStateManager.pushMatrix();
							GlStateManager.translate(0.0f, 0.0f, offset);
							GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
							float radius = 1 + offset * 4;
							GlStateManager.translate(0.0f, radius * 0.2, 0.0f);
							GlStateManager.rotate(angle, 0.0f, 0.0f, -1.0f);
							bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
							ClientUtils.addTexturedBoxVertices(bufferbuilder, voxel, 1.0f, 1.0f, 1.0f, 0.5f);
							tessellator.draw();
							GlStateManager.popMatrix();
						}
					}

					GlStateManager.disableAlpha();
					GlStateManager.disableBlend();
					GlStateManager.enableCull();
					GlStateManager.enableLighting();
					GlStateManager.enableTexture2D();
					GlStateManager.popMatrix();
					LCRenderHelper.restoreLightmapTextureCoords();
				}
			}
		}

		@SubscribeEvent
		public static void onRenderLayer(RenderSuperpowerLayerEvent e)
		{
			if (Minecraft.getMinecraft().player == null)
				return;

			EntityPlayer player = e.getPlayer();
			AxisAlignedBB voxel = new AxisAlignedBB(-0.025, -0.025, -0.025, 0.025, 0.025, 0.025);

			for (AbilityScreech ab : Ability.getAbilitiesFromClass(Ability.getCurrentAbilities(player), AbilityScreech.class))
			{
				if (ab != null && ab.isUnlocked() && ab.isEnabled())
				{
					LCRenderHelper.setLightmapTextureCoords(240, 240);
					GlStateManager.pushMatrix();
					GlStateManager.disableLighting();
					GlStateManager.disableTexture2D();
					GlStateManager.disableCull();
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					e.getRenderPlayer().getMainModel().bipedHead.postRender(e.getScale());
					GlStateManager.translate(0, -0.1, -0.5);

					for (int i = 0; i < 7 && i < ab.ticks; i++)
					{
						float offset = ((float) i) + ((ab.ticks % 4 + e.getPartialTicks()) / 5f);
						for (int angle = 0; angle < 360; angle += 8 - i)
						{
							GlStateManager.pushMatrix();
							GlStateManager.translate(0.0f, 0.0f, -offset);
							GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
							float radius = 1 + offset * 3;
							GlStateManager.translate(0.0f, radius * 0.2, 0.0f);
							GlStateManager.rotate(angle, 0.0f, 0.0f, -1.0f);
							bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
							ClientUtils.addTexturedBoxVertices(bufferbuilder, voxel, 1.0f, 1.0f, 1.0f, 0.5f);
							tessellator.draw();
							GlStateManager.popMatrix();
						}
					}

					GlStateManager.disableAlpha();
					GlStateManager.disableBlend();
					GlStateManager.enableCull();
					GlStateManager.enableLighting();
					GlStateManager.enableTexture2D();
					GlStateManager.popMatrix();
					LCRenderHelper.restoreLightmapTextureCoords();
				}
			}
		}

	}
}
