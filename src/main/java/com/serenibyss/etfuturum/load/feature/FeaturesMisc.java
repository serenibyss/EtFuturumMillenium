package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.ConfigBlocksItems;

/** Features too generic or too spread out to include in any specific version of Minecraft. */
public class FeaturesMisc extends FeatureManager {

    public final Feature newStairs;

    protected FeaturesMisc() {
        newStairs = Feature.builder("new_stairs", this, () -> ConfigBlocksItems.enableNewStairs).build();
    }

    @Override
    public MCVersion getMinecraftVersion() {
        // Pick latest to cover any case; can manually specify a different MC version for assets as needed.
        return MCVersion.MC1_20;
    }
}
