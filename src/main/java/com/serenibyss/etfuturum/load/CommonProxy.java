package com.serenibyss.etfuturum.load;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.client.GuiHandler;
import com.serenibyss.etfuturum.entities.EFMEntities;
import com.serenibyss.etfuturum.event.EventHandler;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.recipes.EFMRecipes;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EFMSounds.class);
        MinecraftForge.EVENT_BUS.register(EFMBlocks.class);
        MinecraftForge.EVENT_BUS.register(EFMItems.class);
        MinecraftForge.EVENT_BUS.register(EFMRecipes.class);
        MinecraftForge.EVENT_BUS.register(EFMEntities.class);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        NetworkRegistry.INSTANCE.registerGuiHandler(EFMTags.MODID, new GuiHandler());
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }
}
