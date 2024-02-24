package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;
import com.serenibyss.etfuturum.load.config.ConfigEntities;

public class Features13 extends FeatureManager {

    public final Feature phantom;
    public final Feature stripping;
    public final Feature conduit;
    public final Feature trident;

    public final Feature turtle;
    public final Feature fish;

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

        stripping = Feature.builder("stripping", this, () -> ConfigBlocksItems.enableStripping)
                .addTextures(MCVersion.MC1_14, "block/stripped_acacia_log.png",
                        "block/stripped_acacia_log_top.png", "block/stripped_birch_log.png",
                        "block/stripped_birch_log_top.png", "block/stripped_dark_oak_log.png",
                        "block/stripped_dark_oak_log_top.png", "block/stripped_jungle_log.png",
                        "block/stripped_jungle_log_top.png", "block/stripped_oak_log.png",
                        "block/stripped_oak_log_top.png", "block/stripped_spruce_log.png",
                        "block/stripped_spruce_log_top.png")
                .addNumberedSounds("item/axe/strip", 1, 4)
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

        turtle = Feature.builder("turtle", this, () -> ConfigEntities.enableTurtles)
                .addTextures("entity/turtle/big_sea_turtle.png", "item/turtle_egg.png",
                        "block/turtle_egg.png", "block/turtle_egg_very_cracked.png",
                        "block/turtle_egg_slightly_cracked.png", "item/scute.png",
                        "item/turtle_helmet.png", "models/armor/turtle_layer_1.png")
                .addSounds("mob/turtle/armor")
                .addNumberedSounds("mob/turtle/idle", 1, 3)
                .addNumberedSounds("mob/turtle/death", 1, 3)
                .addNumberedSounds("mob/turtle/baby/death", 1, 2)
                .addNumberedSounds("mob/turtle/egg/egg_break", 1, 2)
                .addNumberedSounds("mob/turtle/egg/egg_crack", 1, 5)
                .addNumberedSounds("mob/turtle/baby/egg_hatched", 1, 3)
                .addNumberedSounds("mob/turtle/hurt", 1, 5)
                .addNumberedSounds("mob/turtle/baby/hurt", 1, 2)
                .addNumberedSounds("mob/turtle/egg/drop_egg", 1, 2)
                .addNumberedSounds("mob/turtle/walk", 1, 5)
                .addNumberedSounds("mob/turtle/baby/shamble", 1, 4)
                .addNumberedSounds("mob/turtle/swim/swim", 1, 5)
                .build();

        fish = Feature.builder("fish", this, () -> ConfigEntities.enableFish)
                .addTextures("entity/fish/cod.png", "item/cod_bucket.png",
                        "entity/fish/salmon.png", "item/salmon_bucket.png",
                        "entity/fish/pufferfish.png", "item/pufferfish_bucket.png"
                        )
                .addNumberedSounds("entity/fish/hurt", 1, 4)
                .addNumberedSounds("entity/fish/flop", 1, 4)
                .addNumberedSounds("entity/pufferfish/blow_out", 1, 2)
                .addNumberedSounds("entity/pufferfish/blow_up", 1, 2)
                .addNumberedSounds("entity/pufferfish/death", 1, 2)
                .addNumberedSounds("entity/pufferfish/flop", 1, 4)
                .addNumberedSounds("entity/pufferfish/hurt", 1, 2)
                .addNumberedSounds("entity/pufferfish/sting", 1, 2)
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_13;
    }
}
