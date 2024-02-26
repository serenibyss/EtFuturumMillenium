package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;

public class Features18 extends FeatureManager {

    public final Feature deepslate;
    public Features18() {
        deepslate = Feature.builder("deepslate", this, () -> ConfigBlocksItems.enableDeepslate)
                .addTextures("block/deepslate.png", "block/deepslate_top.png")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_18;
    }
}
