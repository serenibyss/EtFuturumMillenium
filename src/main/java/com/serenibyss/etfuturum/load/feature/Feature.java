package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.config.EtFuturumConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Feature {

    private final FeatureManager myManager;
    private final Supplier<Boolean> enabledTest;

    protected final Set<String> textures;
    protected final Set<String> sounds;
    protected final Set<String> structures;

    @Nullable
    protected String mixinPackage;

    private Feature(FeatureManager myManager, Supplier<Boolean> enabledTest,
                    Set<String> textures, Set<String> sounds, Set<String> structures,
                    @Nullable String mixinPackage) {
        this.myManager = myManager;
        this.enabledTest = enabledTest;
        this.textures = textures;
        this.sounds = sounds;
        this.structures = structures;
        this.mixinPackage = mixinPackage;

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

        private final FeatureManager manager;
        private final Supplier<Boolean> enabledTest;

        private final Set<String> textures = new HashSet<>();
        private final Set<String> sounds = new HashSet<>();
        private final Set<String> structures = new HashSet<>();

        private String mixinPackage;

        private Builder(FeatureManager manager, Supplier<Boolean> enabledTest) {
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

        /**
         * Register mixins for this feature. Used instead of adding them to a Mixin cfg JSON.
         *
         * @param mixinPackage The package. Assumes that it will be located at {@link com.serenibyss.etfuturum.mixin}
         *                     and will not have any sub-packages.
         */
        public Builder addMixinPackage(String mixinPackage) {
            this.mixinPackage = mixinPackage;
            return this;
        }

        public Feature build() {
            return new Feature(manager, enabledTest, textures, sounds, structures, mixinPackage);
        }
    }
}
