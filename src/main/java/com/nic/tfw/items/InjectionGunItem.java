package com.nic.tfw.items;

import com.nic.tfw.TFWMod;
import com.nic.tfw.component.TFWDataComponents;
import com.nic.tfw.genetics.GeneticData;
import com.nic.tfw.genetics.GeneticUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = TFWMod.MODID)
public class InjectionGunItem extends Item {

    public InjectionGunItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (onRightClick(player, hand)) return InteractionResult.SUCCESS;
        return super.use(level, player, hand);
    }

    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        var item = event.getEntity().getMainHandItem();
        if (item.getItem() == TFWItems.INJECTION_GUN.asItem()) {
            GeneState state = GeneState.getState(item);
            if (event.getEntity().level().isClientSide() || state != GeneState.EMPTY) return;
            if (event.getTarget() instanceof LivingEntity livingTarget)
                item.set(TFWDataComponents.GENETIC_DATA, GeneticUtil.createGeneticData(livingTarget));
        }
    }

    private boolean onRightClick(LivingEntity clicker, InteractionHand hand) {
        if (clicker.level().isClientSide()) return false;
        InteractionHand off = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack stack = clicker.getItemInHand(hand);
        ItemStack stackOff = clicker.getItemInHand(off);

        GeneState state = GeneState.getState(stack);
        GeneState stateOff = GeneState.getState(stackOff);

        List<GeneticData> geneticData = stack.get(TFWDataComponents.GENETIC_DATA);
        List<GeneticData> geneticDataOff = stackOff.get(TFWDataComponents.GENETIC_DATA);

        if (stackOff.getItem() == TFWItems.VIAL.asItem() && geneticData == null) {
            if (geneticDataOff == null || geneticDataOff.isEmpty()) {
                stack.set(TFWDataComponents.GENETIC_DATA, geneticDataOff != null ? geneticDataOff : new ArrayList<>());
                clicker.setItemInHand(off, ItemStack.EMPTY);
            } else if (stateOff != GeneState.DNA) {
                stack.set(TFWDataComponents.GENETIC_DATA, geneticDataOff);
                clicker.setItemInHand(off, ItemStack.EMPTY);
            }
            return true;
        } else if (stackOff.isEmpty()) {
            if (clicker.isCrouching() && state == GeneState.EMPTY) {
                stack.set(TFWDataComponents.GENETIC_DATA, GeneticUtil.createGeneticData(clicker));
                return true;
            } else if(geneticData != null) {
                ItemStack vial = new ItemStack(TFWItems.VIAL.asItem());
                vial.set(TFWDataComponents.GENETIC_DATA, geneticData);
                clicker.setItemInHand(off, vial);
                stack.remove(TFWDataComponents.GENETIC_DATA);
                return true;
            }
        }
        return false;
    }
}
