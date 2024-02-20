package com.serenibyss.etfuturum.load;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.advancement.EFMAdvancements;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.client.GuiHandler;
import com.serenibyss.etfuturum.enchantment.EFMEnchantments;
import com.serenibyss.etfuturum.entities.EFMEntities;
import com.serenibyss.etfuturum.event.EventHandler;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.loot.EFMLootTables;
import com.serenibyss.etfuturum.recipes.EFMRecipes;
import com.serenibyss.etfuturum.rule.EFMGameRules;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.stats.EFMStatList;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Map;

@SuppressWarnings("unused")
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EFMSounds.class);
        MinecraftForge.EVENT_BUS.register(EFMBlocks.class);
        MinecraftForge.EVENT_BUS.register(EFMItems.class);
        MinecraftForge.EVENT_BUS.register(EFMRecipes.class);
        MinecraftForge.EVENT_BUS.register(EFMEntities.class);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.register(EFMAdvancements.class);
        MinecraftForge.EVENT_BUS.register(EFMGameRules.class);
        MinecraftForge.EVENT_BUS.register(EFMEnchantments.class);


        NetworkRegistry.INSTANCE.registerGuiHandler(EFMTags.MODID, new GuiHandler());

        EFMAdvancements.initTriggers();
    }

    public void init(FMLInitializationEvent event) {
        EFMStatList.init();
        EFMLootTables.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    public void registerItemRendererWithOverride(Block block, Map<IProperty<?>, Comparable<?>> stateOverrides) {
    }

    public boolean isClientSide() {
        return false;
    }
}
