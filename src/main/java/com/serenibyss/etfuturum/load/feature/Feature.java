package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.EtFuturumConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Feature {

    private final FeatureManager myManager;
    private final Supplier<Boolean> enabledTest;

    protected final Set<String> textures = new HashSet<>();
    protected final Set<String> sounds = new HashSet<>();
    protected final Set<String> structures = new HashSet<>();

    private Feature(FeatureManager myManager, Supplier<Boolean> enabledTest) {
        this.myManager = myManager;
        this.enabledTest = enabledTest;
        this.myManager.registerFeature(this);
    }

    public boolean isEnabled() {
        if (myManager.getMinecraftVersion().ordinal() > EtFuturumConfig.allFeaturesUpToVersion.ordinal()) {
            return false;
        }
        return enabledTest.get();
    }

    public static Feature.Builder builder(FeatureManager manager, Supplier<Boolean> enabledTest) {
        return new Builder(manager, enabledTest);
    }

    public static class Builder {

        private final Feature feature;

        private Builder(FeatureManager manager, Supplier<Boolean> enabledTest) {
            feature = new Feature(manager, enabledTest);
        }

        public Builder addTextures(String... textures) {
            feature.textures.addAll(Arrays.asList(textures));
            return this;
        }

        public Builder addSounds(String... sounds) {
            feature.sounds.addAll(Arrays.asList(sounds));
            return this;
        }

        public Builder addStructures(String... structures) {
            feature.structures.addAll(Arrays.asList(structures));
            return this;
        }

        public Feature build() {
            return feature;
        }
    }
}
