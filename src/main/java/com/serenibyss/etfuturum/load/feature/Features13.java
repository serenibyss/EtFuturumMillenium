package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;
import com.serenibyss.etfuturum.load.config.ConfigEntities;

public class Features13 extends FeatureManager {

    public final Feature phantom;
    public final Feature conduit;
    public final Feature trident;

    public Features13() {
        phantom = Feature.builder("phantom", this, () -> ConfigEntities.enablePhantoms)
                .addTextures("entity/phantom.png", "entity/phantom_eyes.png", "item/phantom_membrane.png")
                .addNumberedSounds("mob/phantom/flap", 1, 6)
                .addNumberedSounds("mob/phantom/hurt", 1, 3)
                .addNumberedSounds("mob/phantom/swoop", 1, 4)
                .addNumberedSounds("mob/phantom/death", 1, 3)
                .addNumberedSounds("mob/phantom/bite", 1, 2)
                .addNumberedSounds("mob/phantom/idle", 1, 5)
                .build();

        conduit = Feature.builder("conduit", this, () -> ConfigBlocksItems.enableConduit)
                .addTextures("entity/conduit/base.png", "entity/conduit/cage.png", "entity/conduit/wind.png",
                        "entity/conduit/wind_vertical.png", "entity/conduit/open_eye.png",
                        "entity/conduit/closed_eye.png", "block/conduit.png")
                .addTextures(MCVersion.MC1_14, "particle/nautilus.png") // 1.13 particle textures are a sprite sheet, so use 1.14
                .addSounds("block/conduit/activate", "block/conduit/ambient", "block/conduit/deactivate")
                .addNumberedSounds("block/conduit/short", 1, 9)
                .addNumberedSounds("block/conduit/attack", 1, 3)
                .build();

        trident = Feature.builder("trident", this, () -> ConfigBlocksItems.enableTridents)
                .addTextures("entity/trident.png", "item/trident.png")
                .addNumberedSounds("item/trident/pierce", 1, 3)
                .addNumberedSounds("item/trident/ground_impact", 1, 4)
                .addNumberedSounds("item/trident/return", 1, 3)
                .addNumberedSounds("item/trident/riptide", 1, 3)
                .addNumberedSounds("item/trident/throw", 1, 2)
                .addNumberedSounds("item/trident/thunder", 1, 2)
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_13;
    }
}
