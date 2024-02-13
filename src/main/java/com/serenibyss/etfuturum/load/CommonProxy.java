package com.serenibyss.etfuturum.load;

import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.recipes.EFMRecipes;
import com.serenibyss.etfuturum.sounds.EtFuturumSounds;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EtFuturumSounds.class);
        MinecraftForge.EVENT_BUS.register(EFMBlocks.class);
        MinecraftForge.EVENT_BUS.register(EFMRecipes.class);
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }
}
