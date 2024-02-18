package com.serenibyss.etfuturum.entities;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.RenderPhantom;
import com.serenibyss.etfuturum.client.render.entity.RenderTrident;
import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EFMEntities {

    static int id = 0;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        if(Features.MC13.phantom.isEnabled()) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityPhantom.class)
                    .id(new ResourceLocation(EFMTags.MODID, "phantom"), id++)
                    .tracker(80, 3, true)
                    .name("phantom")
                    .egg(0x43518A, 0x88FF00).build());
        }
        if(Features.MC13.trident.isEnabled()) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityTrident.class)
                    .id(new ResourceLocation(EFMTags.MODID, "trident"), id++)
                    .tracker(64, 2, true)
                    .name("trident")
                    .build());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        if(Features.MC13.phantom.isEnabled()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, RenderPhantom::new);
        }
        if(Features.MC13.trident.isEnabled()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);
        }
    }
}
