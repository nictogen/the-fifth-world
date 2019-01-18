package com.nic.tfw.potion;

import com.nic.tfw.TheFifthWorld;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.karma.ModuleKarma;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 1/17/19.
 */
@Mod.EventBusSubscriber(modid = LucraftCore.MODID)
public class PotionExplode extends Potion
{

	@SubscribeEvent
	public static void onRegisterPotions(RegistryEvent.Register<Potion> e) {
		if (ModuleKarma.INSTANCE.isEnabled()) {
			e.getRegistry().register(POTION_EXPLODE);
		}
	}

	public static PotionExplode POTION_EXPLODE = new PotionExplode();

	public PotionExplode() {
		super(true, 0xD3D3D3);
		this.setPotionName("potion.explode");
		this.setRegistryName(new ResourceLocation(TheFifthWorld.MODID, "explode"));
	}

	@Override
	public boolean shouldRender(PotionEffect effect) {
		return true;
	}

	/**
	 * Returns true if the potion has a associated status icon to display in then inventory when active.
	 */
	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@Mod.EventBusSubscriber
	public static class Handler {

		@SubscribeEvent
		public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event){
			if(event.getEntityLiving().isPotionActive(POTION_EXPLODE) && event.getEntityLiving().getActivePotionEffect(POTION_EXPLODE).getDuration() == 1){
				event.getEntityLiving().world.createExplosion(null, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 1.0f, true);
				event.getEntityLiving().setDead();
			}
		}
	}
}
