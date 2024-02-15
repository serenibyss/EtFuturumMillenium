package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigEntities;

public class Features13 extends FeatureManager {

    public final Feature phantom;

    public Features13() {
        phantom = Feature.builder("phantom", this, () -> ConfigEntities.enablePhantoms)
                .addTextures("entity/phantom.png", "entity/phantom_eyes.png", "item/phantom_membrane.png")
                .addSounds("mob/phantom/flap1.ogg","mob/phantom/flap2.ogg","mob/phantom/flap3.ogg",
                        "mob/phantom/flap4.ogg","mob/phantom/flap5.ogg","mob/phantom/flap6.ogg",
                        "mob/phantom/hurt1.ogg", "mob/phantom/hurt2.ogg", "mob/phantom/hurt3.ogg",
                        "mob/phantom/swoop1.ogg", "mob/phantom/swoop2.ogg", "mob/phantom/swoop3.ogg", "mob/phantom/swoop4.ogg",
                        "mob/phantom/death1.ogg", "mob/phantom/death2.ogg", "mob/phantom/death3.ogg",
                        "mob/phantom/bite1.ogg", "mob/phantom/bite2.ogg",
                        "mob/phantom/idle1.ogg", "mob/phantom/idle2.ogg", "mob/phantom/idle3.ogg", "mob/phantom/idle4.ogg",
                        "mob/phantom/idle5.ogg")
                .build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        return MCVersion.MC1_13;
    }
}
