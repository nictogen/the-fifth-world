package com.nic.tfw.superpower.genes;

import com.nic.tfw.superpower.abilities.AbilityIronSkin;
import com.nic.tfw.superpower.abilities.AbilitySizeChangeChild;
import com.nic.tfw.superpower.sizechangers.IronSkinSizeChanger;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilitySizeChange;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityTogglePower;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityConditionAbility;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 2019-03-13.
 */
public class GeneIronSkin extends Gene
{
	public GeneIronSkin(String displayName)
	{
		super(AbilityTogglePower.class, displayName, true);
	}

	@Override public void postAbilityCreation(EntityLivingBase entityLivingBase, Ability.AbilityMap abilityList, GeneSet.GeneData data)
	{
		AbilityTogglePower togglePower = (AbilityTogglePower) abilityList.get("gene_" + getRegistryName().toString());
		AbilityIronSkin a = new AbilityIronSkin(entityLivingBase);
		a.setParentAbility(togglePower);
		AbilitySizeChange a2 = new AbilitySizeChangeChild(entityLivingBase);
		a2.getDataManager().set(AbilitySizeChange.SIZE, 1.5f);
		a2.getDataManager().set(AbilitySizeChange.SIZE_CHANGER, IronSkinSizeChanger.IRON_SKIN_SIZE_CHANGER);
		a2.getDataManager().set(Ability.SHOW_IN_BAR, false);
		a2.addCondition(new AbilityConditionAbility(togglePower));
		a2.setParentAbility(togglePower);
		abilityList.put("gene_" + getRegistryName().toString() + "_" + a2.getAbilityEntry().getRegistryName().toString(), a2);
		abilityList.put("gene_" + getRegistryName().toString() + "_" + a.getAbilityEntry().getRegistryName().toString(), a);
	}
}
