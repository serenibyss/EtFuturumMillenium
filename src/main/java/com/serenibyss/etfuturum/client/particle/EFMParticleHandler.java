package com.serenibyss.etfuturum.client.particle;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMParticleHandler {

    protected static TextureAtlasSprite NAUTILUS;

    @SubscribeEvent
    public static void registerParticleSprites(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        if (!map.getBasePath().equals("textures")) {
            return;
        }

        NAUTILUS = registerSprite(MC13.conduit, map, "particle/nautilus");
    }

    private static TextureAtlasSprite registerSprite(Feature feature, TextureMap map, String texture) {
        if (!feature.isEnabled()) {
            return null;
        }
        ResourceLocation location = new ResourceLocation(EFMTags.MODID, texture);
        return map.registerSprite(location);
    }
}
