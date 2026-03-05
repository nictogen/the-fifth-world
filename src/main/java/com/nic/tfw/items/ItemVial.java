package com.nic.tfw.items;

import com.nic.tfw.component.TFWDataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class ItemVial extends Item {
    public ItemVial(Properties properties) {
        super(properties
                .stacksTo(16)
                .component(TFWDataComponents.GENETIC_DATA, new ArrayList<>()));
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        InteractionHand off = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack stack = player.getItemInHand(hand);
        ItemStack stackOff = player.getItemInHand(off);
        if(stackOff.getItem() == TFWItems.INJECTION_GUN.asItem()) return super.use(level, player, hand);
        if(player.isCrouching())
            stack.set(TFWDataComponents.GENETIC_DATA, new ArrayList<>());
        return super.use(level, player, hand);
    }
}
