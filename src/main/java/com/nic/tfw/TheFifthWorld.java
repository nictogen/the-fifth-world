package com.nic.tfw;

import com.nic.tfw.blocks.centrifuge.BlockCentrifuge;
import com.nic.tfw.blocks.centrifuge.TileEntityCentrifuge;
import com.nic.tfw.blocks.microscope.BlockMicroscope;
import com.nic.tfw.blocks.microscope.TileEntityMicroscope;
import com.nic.tfw.client.GuiHandler;
import com.nic.tfw.items.ItemInjectionGun;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.proxy.CommonProxy;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import com.nic.tfw.superpower.abilities.*;
import com.nic.tfw.superpower.abilities.defects.*;
import com.nic.tfw.superpower.genes.GeneHandler;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEntry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;

@Mod(modid = TheFifthWorld.MODID, name = TheFifthWorld.NAME, version = TheFifthWorld.VERSION, dependencies = TheFifthWorld.DEPENDENCIES)
@Mod.EventBusSubscriber
public class TheFifthWorld
{
    public static final String MODID = "the-fifth-world";
    public static final String NAME = "The Fifth World";
    public static final String VERSION = "0.5";
	public static final String DEPENDENCIES = "required-after:lucraftcore@[1.12.2-2.4.0,); after:heroesexpansion@[1.12.2-1.3.2,)";

	@SidedProxy(clientSide = "com.nic.tfw.proxy.ClientProxy", serverSide = "com.nic.tfw.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance
    public static TheFifthWorld INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
	    GameRegistry.registerTileEntity(TileEntityMicroscope.class, new ResourceLocation(MODID , ":microscope"));
	    GameRegistry.registerTileEntity(TileEntityCentrifuge.class, new ResourceLocation(MODID , ":centrifuge"));
	    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	    proxy.onInit(event);
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		GeneHandler.populateGeneList();
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new BlockMicroscope());
		event.getRegistry().register(new BlockCentrifuge());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) throws IllegalAccessException
	{
		for (Field field : Blocks.class.getDeclaredFields())
		{
			Block block = (Block) field.get(null);
			Item itemBlock = new ItemBlock(block).setRegistryName(block.getRegistryName()).setTranslationKey(block.getTranslationKey());
			event.getRegistry().register(itemBlock);
		}
		event.getRegistry().register(new ItemVial());
		event.getRegistry().register(new ItemInjectionGun());
	}

	@SubscribeEvent
	public static void registerSuperpowers(RegistryEvent.Register<Superpower> event)
	{
		event.getRegistry().register(new SuperpowerGeneticallyModified());
	}

	@SubscribeEvent
	public static void registerAbilities(RegistryEvent.Register<AbilityEntry> event)
	{
		event.getRegistry().register(new AbilityEntry(InvoluntaryExplosion.class, new ResourceLocation(MODID, "involuntary_explosion")));
		event.getRegistry().register(new AbilityEntry(SetOnFire.class, new ResourceLocation(MODID, "set_on_fire")));
		event.getRegistry().register(new AbilityEntry(ButterFingers.class, new ResourceLocation(MODID, "butter_fingers")));
		event.getRegistry().register(new AbilityEntry(RestrictiveDiet.class, new ResourceLocation(MODID, "restrictive_diet")));
		event.getRegistry().register(new AbilityEntry(Deafness.class, new ResourceLocation(MODID, "deafness")));
		event.getRegistry().register(new AbilityEntry(LightningRod.class, new ResourceLocation(MODID, "lightning_rod")));
		event.getRegistry().register(new AbilityEntry(Starving.class, new ResourceLocation(MODID, "starving")));
		event.getRegistry().register(new AbilityEntry(AbilityMakeHostile.class, new ResourceLocation(MODID, "make_hostile")));
		event.getRegistry().register(new AbilityEntry(AbilityScreech.class, new ResourceLocation(MODID, "screech")));
		event.getRegistry().register(new AbilityEntry(AbilityLayEgg.class, new ResourceLocation(MODID, "lay_egg")));
		event.getRegistry().register(new AbilityEntry(AbilityEatBlock.class, new ResourceLocation(MODID, "eat_block")));
		event.getRegistry().register(new AbilityEntry(AbilityPotionImmunity.class, new ResourceLocation(MODID, "potion_immunity")));
		event.getRegistry().register(new AbilityEntry(AbilityExplode.class, new ResourceLocation(MODID, "explode")));
		event.getRegistry().register(new AbilityEntry(AbilityMountable.class, new ResourceLocation(MODID, "mountable")));
		event.getRegistry().register(new AbilityEntry(AbilityItemCreation.class, new ResourceLocation(MODID, "create_item")));
		event.getRegistry().register(new AbilityEntry(AbilityGivePotion.class, new ResourceLocation(MODID, "give_potion")));
		event.getRegistry().register(new AbilityEntry(AbilityChangeItem.class, new ResourceLocation(MODID, "change_item")));
		event.getRegistry().register(new AbilityEntry(AbilityBonemealArea.class, new ResourceLocation(MODID, "bonemeal_area")));
		event.getRegistry().register(new AbilityEntry(AbilitySummonEntity.class, new ResourceLocation(MODID, "summon_entity")));
	}

    @GameRegistry.ObjectHolder(MODID)
    public static class Blocks {
	    public static final Block microscope = null;
	    public static final Block centrifuge = null;
    }

	@GameRegistry.ObjectHolder(MODID)
	public static class Items {
		public static final Item glass_vial = null;
		public static final Item injection_gun = null;
	}

	@GameRegistry.ObjectHolder(MODID)
	public static class Superpowers {
		public static final Superpower genetically_modified = null;
	}

    public enum GUI {
    	MICROSCOPE,
	    CENTRIFUGE
    }
}
