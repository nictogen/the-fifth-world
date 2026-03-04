package com.nic.tfw.items;

import com.nic.tfw.TFWMod;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = TFWMod.MODID)
public class InjectionGunItem extends Item {

    public InjectionGunItem(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event){
        if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof InjectionGunItem injection){
            injection.onLeftClick(event.getEntity(), null, event.getHand());
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){

        if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof InjectionGunItem injection){
            event.setCanceled(!injection.onLeftClick(event.getEntity(), null, event.getHand()));
        }
    }

    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event){
        var item = event.getEntity().getMainHandItem();
        if(!item.isEmpty() && item.getItem() instanceof InjectionGunItem injection){
            event.setCanceled(!injection.onLeftClick(event.getEntity(), event.getTarget(), InteractionHand.MAIN_HAND));
        }
    }

    private boolean onLeftClick(LivingEntity clicker, @Nullable Entity target, InteractionHand hand){
        if(clicker.isCrouching()){
            ItemStack s = clicker.getItemInHand(hand);
            s.set(TFWDataComponents.GENETIC_DATA, GeneticUtil.createGeneticData(clicker));
            clicker.setItemInHand(hand, s);
            return false;
        }

        return true;
    }
}
