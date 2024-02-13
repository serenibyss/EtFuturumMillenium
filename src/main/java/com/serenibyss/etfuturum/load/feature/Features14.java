package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;

public class Features14 extends FeatureManager {

    public final Feature barrel;

    public Features14() {
        // initialize your features here
        barrel = Feature.builder(this, () -> ConfigBlocksItems.enableBarrels)
                .addTextures("block/barrel_bottom.png", "block/barrel_side.png", "block/barrel_top.png", "block/barrel_top_open.png")
                .addSounds("block/barrel/open1.ogg", "block/barrel/open2.ogg", "block/barrel/close.ogg")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_14;
    }
}
