//package com.nic.tfw.superpower.conditions;
//
//import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
//import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
//import net.minecraft.util.text.ITextComponent;
//
///**
// * Created by Nictogen on 1/17/19.
// */
//public abstract class OneUseCondition extends AbilityCondition
//{
//	private boolean isEnabled = false;
//
//	public OneUseCondition(ITextComponent displayText)
//	{
//		super(null, displayText);
//	}
//
//	@Override public boolean test(Ability ability)
//	{
//		if(isEnabled){
//			isEnabled = false;
//			return true;
//		} else return false;
//	}
//
//	public void setEnabled()
//	{
//		isEnabled = true;
//	}
//}
