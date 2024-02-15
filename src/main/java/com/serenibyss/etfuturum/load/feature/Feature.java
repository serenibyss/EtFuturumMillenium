package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.EtFuturumConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Feature {

    private final String name;
    private final FeatureManager myManager;
    private final Supplier<Boolean> enabledTest;

    protected final Set<String> textures;
    protected final Set<String> sounds;
    protected final Set<String> structures;

    private Feature(String name, FeatureManager myManager, Supplier<Boolean> enabledTest,
                    Set<String> textures, Set<String> sounds, Set<String> structures) {
        this.name = name;
        this.myManager = myManager;
        this.enabledTest = enabledTest;
        this.textures = textures;
        this.sounds = sounds;
        this.structures = structures;

        this.myManager.registerFeature(this.name, this);
    }

    public boolean isEnabled() {
        if (myManager.getMinecraftVersion().ordinal() > EtFuturumConfig.allFeaturesUpToVersion.ordinal()) {
            return false;
        }
        return enabledTest.get();
    }

    @Override
    public String toString() {
        return "Feature:{" + name + "}";
    }

    public static Feature.Builder builder(String name, FeatureManager manager, Supplier<Boolean> enabledTest) {
        return new Builder(name, manager, enabledTest);
    }

    public static class Builder {

        private final String name;
        private final FeatureManager manager;
        private final Supplier<Boolean> enabledTest;

        private final Set<String> textures = new HashSet<>();
        private final Set<String> sounds = new HashSet<>();
        private final Set<String> structures = new HashSet<>();

        private Builder(String name, FeatureManager manager, Supplier<Boolean> enabledTest) {
            this.name = name;
            this.manager = manager;
            this.enabledTest = enabledTest;
        }

        public Builder addTextures(String... textures) {
            this.textures.addAll(Arrays.asList(textures));
            return this;
        }

        public Builder addSounds(String... sounds) {
            this.sounds.addAll(Arrays.asList(sounds));
            return this;
        }

        public Builder addStructures(String... structures) {
            this.structures.addAll(Arrays.asList(structures));
            return this;
        }

        public Feature build() {
            return new Feature(name, manager, enabledTest, textures, sounds, structures);
        }
    }
}
