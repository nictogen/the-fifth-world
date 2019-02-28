package com.nic.tfw.he;

import com.nic.tfw.superpower.genes.Gene;
import com.nic.tfw.superpower.genes.GeneHandler;
import lucraft.mods.heroesexpansion.abilities.*;

import java.lang.reflect.Field;

/**
 * Created by Nictogen on 2019-02-27.
 */
public class HEGenes
{
	public static Gene blindness;
	public static Gene precision;
	public static Gene energy_absorption;
	public static Gene photon_blast;
	public static Gene heat_vision;
	public static Gene god_mode;
	public static Gene lightning_attack;
	public static Gene lightning_strike;
	public static Gene spider_sense;
	public static Gene wall_crawling;
	public static Gene web_wings;

	public static void populateGeneList(){
		blindness = new Gene(AbilityBlindness.class, "Blindness");
		precision = new Gene(AbilityPrecision.class, "Precision");
		energy_absorption = new Gene(AbilityEnergyAbsorption.class, "Energy Absorption").addDataMod(new Gene.DataMod<>(AbilityEnergyAbsorption.MAX_ENERGY, 50)).addDataMod(new Gene.DataMod<>(AbilityEnergyAbsorption.MAX_DAMAGE, 5f)).addCooldown(100);
		photon_blast = new GenePhotonBlast().addCooldown(60);
		heat_vision = new GeneHeatVision().addCooldown(250);
		god_mode = new Gene(AbilityGodMode.class, "God Mode").addDataMod(new Gene.DataMod<>(AbilityGodMode.DAMAGE, 3f)).addCooldown(100);
		lightning_attack = new Gene(AbilityLightningAttack.class, "Lightning Attack").addDataMod(new Gene.DataMod<>(AbilityLightningAttack.DISTANCE, 10f)).addCooldown(100);
		lightning_strike = new Gene(AbilityLightningStrike.class, "Lightning Strike").addCooldown(100);
		spider_sense = new Gene(AbilitySpiderSense.class, "Spider Sense");
		wall_crawling = new Gene(AbilityWallCrawling.class, "Wall Crawling");
		web_wings = new Gene(AbilityWebWings.class, "Web Wings");
		for (Field field : HEGenes.class.getFields())
			try { GeneHandler.GENE_REGISTRY.register((Gene) field.get(null)); }
			catch (IllegalAccessException e) { e.printStackTrace(); }
	}
}
