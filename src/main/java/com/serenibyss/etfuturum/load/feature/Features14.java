package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;

public class Features14 extends FeatureManager {

    public final Feature barrel;
    public final Feature stonecutter;

    public Features14() {
        // initialize your features here
        barrel = Feature.builder(this, () -> ConfigBlocksItems.enableBarrels)
                .addTextures("block/barrel_bottom.png", "block/barrel_side.png", "block/barrel_top.png",
                        "block/barrel_top_open.png", "gui/container/shulker_box.png")
                .addSounds("block/barrel/open1.ogg", "block/barrel/open2.ogg", "block/barrel/close.ogg")
                .build();

        stonecutter = Feature.builder(this, () -> ConfigBlocksItems.enableStonecutter)
                .addTextures("block/stonecutter_bottom.png", "block/stonecutter_top.png", "block/stonecutter_side.png",
                        "block/stonecutter_saw.png", "block/stonecutter_saw.png.mcmeta", "gui/container/stonecutter.png")
                .addSounds("ui/stonecutter/cut1.ogg", "ui/stonecutter/cut2.ogg")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_14;
    }
}
