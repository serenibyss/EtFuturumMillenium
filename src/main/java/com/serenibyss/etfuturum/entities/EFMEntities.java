package com.serenibyss.etfuturum.entities;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.*;
import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import com.serenibyss.etfuturum.entities.passive.EntityTurtle;
import com.serenibyss.etfuturum.entities.passive.fish.EntityCod;
import com.serenibyss.etfuturum.entities.passive.fish.EntityPufferfish;
import com.serenibyss.etfuturum.entities.passive.fish.EntitySalmon;
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
        if(Features.MC13.turtle.isEnabled()) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityTurtle.class)
                    .id(new ResourceLocation(EFMTags.MODID, "turtle"), id++)
                    .tracker(80, 3, true)
                    .name("turtle")
                    .egg(0xE7E7E7, 0x00AFAF).build());
        }
        if(Features.MC13.fish.isEnabled()) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityCod.class)
                    .id(new ResourceLocation(EFMTags.MODID, "cod"), id++)
                    .tracker(80, 3, true)
                    .name("cod")
                    .egg(0xE7E7E7, 0x00AFAF).build());
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntitySalmon.class)
                    .id(new ResourceLocation(EFMTags.MODID, "salmon"), id++)
                    .tracker(80, 3, true)
                    .name("salmon")
                    .egg(0xE7E7E7, 0x00AFAF).build());
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityPufferfish.class)
                    .id(new ResourceLocation(EFMTags.MODID, "pufferfish"), id++)
                    .tracker(80, 3, true)
                    .name("pufferfish")
                    .egg(0xE7E7E7, 0x00AFAF).build());
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
        if(Features.MC13.turtle.isEnabled()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, RenderTurtle::new);
        }
        if(Features.MC13.fish.isEnabled()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityCod.class, RenderCod::new);
            RenderingRegistry.registerEntityRenderingHandler(EntitySalmon.class, RenderSalmon::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityPufferfish.class, RenderPufferfish::new);
        }

    }
}
