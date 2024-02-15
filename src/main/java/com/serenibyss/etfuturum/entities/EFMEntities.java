package com.serenibyss.etfuturum.entities;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.RenderPhantom;
import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
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

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        if(Features.MC13.phantom.isEnabled()) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityPhantom.class)
                    .id(new ResourceLocation(EFMTags.MODID, "phantom"), 0)
                    .tracker(80, 3, false)
                    .name("phantom")
                    .egg(0x0000ff, 0x00ff00).build());
        }


    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        if(Features.MC13.phantom.isEnabled()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, RenderPhantom::new);
        }
    }
}
