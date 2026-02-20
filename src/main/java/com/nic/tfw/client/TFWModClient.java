package com.nic.tfw.client;

import com.nic.tfw.TFWMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

@Mod(value = TFWMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TFWMod.MODID, value = Dist.CLIENT)
public class TFWModClient {

    public TFWModClient(ModContainer container){

    }
}
