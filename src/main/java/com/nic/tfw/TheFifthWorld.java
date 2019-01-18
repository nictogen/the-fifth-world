package com.nic.tfw;

import com.nic.tfw.blocks.centrifuge.BlockCentrifuge;
import com.nic.tfw.blocks.centrifuge.TileEntityCentrifuge;
import com.nic.tfw.blocks.microscope.BlockMicroscope;
import com.nic.tfw.blocks.microscope.TileEntityMicroscope;
import com.nic.tfw.client.GuiHandler;
import com.nic.tfw.items.ItemInjectionGun;
import com.nic.tfw.items.ItemVial;
import com.nic.tfw.proxy.CommonProxy;
import com.nic.tfw.superpower.Gene;
import com.nic.tfw.superpower.SuperpowerGeneticallyModified;
import com.nic.tfw.superpower.abilities.*;
import com.nic.tfw.superpower.conditions.Condition;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
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
    public static final String VERSION = "0.1.2";
	public static final String DEPENDENCIES = "required-after:lucraftcore@[1.12.2-2.3.4,)";


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
		Gene.populateGeneList();
		Condition.populateConditionList();
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
	public static void registerAbilities(RegistryEvent.Register<Ability.AbilityEntry> event)
	{
		event.getRegistry().register(new Ability.AbilityEntry(DefectExplosion.class, new ResourceLocation(MODID, "defect_explosion")));
		event.getRegistry().register(new Ability.AbilityEntry(DefectBurning.class, new ResourceLocation(MODID, "defect_burning")));
		event.getRegistry().register(new Ability.AbilityEntry(DefectButterFingers.class, new ResourceLocation(MODID, "defect_butter_fingers")));
		event.getRegistry().register(new Ability.AbilityEntry(DefectDiet.class, new ResourceLocation(MODID, "defect_diet")));
		event.getRegistry().register(new Ability.AbilityEntry(DefectDisable.class, new ResourceLocation(MODID, "defect_disable")));
		event.getRegistry().register(new Ability.AbilityEntry(DefectEnable.class, new ResourceLocation(MODID, "defect_enable")));
	}

    @GameRegistry.ObjectHolder(MODID)
    public static class Blocks {
	    public static final Block microscope = null;
	    public static final Block centrifuge = null;
    }

	@GameRegistry.ObjectHolder(MODID)
	public static class Items {
		public static final Item vial = null;
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
