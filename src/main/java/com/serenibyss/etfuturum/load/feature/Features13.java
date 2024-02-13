package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigEntities;

public class Features13 extends FeatureManager {

    public final Feature phantom;

    public Features13() {
        phantom = Feature.builder(this, ()-> ConfigEntities.enablePhantoms)
                .addTextures("entity/monster/phantom.png")
                .addSounds("entity/monster/phantom_attack.ogg", "entity/monster/phantom_hurt.ogg")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_13;
    }
}
