package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;

public class Features18 extends FeatureManager {

    public final Feature deepslate;
    public Features18() {
        deepslate = Feature.builder("deepslate", this, () -> ConfigBlocksItems.enableDeepslate)
                .addTextures("block/deepslate.png", "block/deepslate_top.png",
                        "block/chiseled_deepslate.png", "block/cobbled_deepslate.png", "block/cracked_deepslate_bricks.png", "block/cracked_deepslate_tiles.png",
                        "block/deepslate_bricks.png", "block/deepslate_tiles.png", "block/polished_deepslate.png")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_18;
    }
}
