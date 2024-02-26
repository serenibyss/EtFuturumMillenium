package com.serenibyss.etfuturum.entities;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.*;
import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import com.serenibyss.etfuturum.entities.passive.EntityTurtle;
import com.serenibyss.etfuturum.entities.passive.fish.EntityCod;
import com.serenibyss.etfuturum.entities.passive.fish.EntityPufferfish;
import com.serenibyss.etfuturum.entities.passive.fish.EntitySalmon;
import com.serenibyss.etfuturum.entities.passive.fish.EntityTropicalFish;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import com.serenibyss.etfuturum.load.enums.EFMEnumCreatureAttribute;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
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
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 3, 6, Biome.getBiome(0)) // ocean
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 3, 6, Biome.getBiome(24)) // deep ocean
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 3, 6, Biome.getBiome(7)) // river
                    .egg(12691306, 15058059).build());
            EntitySpawnPlacementRegistry.setPlacementType(EntityCod.class, EntityLiving.SpawnPlacementType.IN_WATER);

            event.getRegistry().register(EntityEntryBuilder.create().entity(EntitySalmon.class)
                    .id(new ResourceLocation(EFMTags.MODID, "salmon"), id++)
                    .tracker(80, 3, true)
                    .name("salmon")
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 5, Biome.getBiome(10)) // frozen ocean
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 5, Biome.getBiome(7)) // river
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 5, Biome.getBiome(11)) // frozen river
                    .egg(10489616, 951412).build());
            EntitySpawnPlacementRegistry.setPlacementType(EntitySalmon.class, EntityLiving.SpawnPlacementType.IN_WATER);

            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityPufferfish.class)
                    .id(new ResourceLocation(EFMTags.MODID, "pufferfish"), id++)
                    .tracker(80, 3, true)
                    .name("pufferfish")
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 3, Biome.getBiome(7)) // river
                    .egg(16167425, 3654642).build());
            EntitySpawnPlacementRegistry.setPlacementType(EntityPufferfish.class, EntityLiving.SpawnPlacementType.IN_WATER);

            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityTropicalFish.class)
                    .id(new ResourceLocation(EFMTags.MODID, "tropical_fish"), id++)
                    .tracker(80, 3, true)
                    .name("tropical_fish")
                    .spawn(EnumCreatureType.WATER_CREATURE, 25, 8, 8, Biome.getBiome(7)) // river
                    .egg(15690005, 16775663).build());
            EntitySpawnPlacementRegistry.setPlacementType(EntityTropicalFish.class, EntityLiving.SpawnPlacementType.IN_WATER);

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
            RenderingRegistry.registerEntityRenderingHandler(EntityTropicalFish.class, RenderTropicalFish::new);
        }

    }
}
