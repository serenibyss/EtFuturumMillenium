package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.asset.AssetRequest;
import com.serenibyss.etfuturum.load.asset.AssetType;
import com.serenibyss.etfuturum.load.asset.pack.AssetResourcePack;
import com.serenibyss.etfuturum.load.asset.pack.LangOverridePack;
import com.serenibyss.etfuturum.load.config.EtFuturumConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;

public class Feature {

    private final String name;
    private final FeatureManager myManager;
    private final Supplier<Boolean> enabledTest;

    private final List<Pair<MCVersion, String>> textures;
    private final List<Pair<MCVersion, Pair<String, String>>> textureOverrides;
    private final List<Pair<MCVersion, String>> sounds;
    private final List<Pair<MCVersion, String>> structures;
    private final List<String> langOverrides;

    private Feature(String name, FeatureManager myManager,
                    Supplier<Boolean> enabledTest,
                    List<Pair<MCVersion, String>> textures,
                    List<Pair<MCVersion, Pair<String, String>>> textureOverrides,
                    List<Pair<MCVersion, String>> sounds,
                    List<Pair<MCVersion, String>> structures,
                    List<String> langOverrides) {
        this.name = name;
        this.myManager = myManager;
        this.enabledTest = enabledTest;
        this.textures = textures;
        this.textureOverrides = textureOverrides;
        this.sounds = sounds;
        this.structures = structures;
        this.langOverrides = langOverrides;

        this.myManager.registerFeature(this.name, this);
    }

    public boolean isEnabled() {
        if (myManager.getMinecraftVersion().ordinal() > EtFuturumConfig.allFeaturesUpToVersion.ordinal()) {
            return false;
        }
        return enabledTest.get();
    }

    protected void addAssets(AssetType type, Map<MCVersion, AssetRequest> requestMap) {
        var assets = switch (type) {
            case TEXTURES -> textures;
            case SOUNDS -> sounds;
            case STRUCTURES -> structures;
        };

        for (var entry : assets) {
            MCVersion version = entry.getKey();
            if (version == null) version = myManager.getMinecraftVersion();
            requestMap.get(version).add(type, entry.getValue());
        }

        boolean enabled = isEnabled();
        if (type == AssetType.TEXTURES) {
            for (var entry : textureOverrides) {
                MCVersion version = entry.getKey();
                if (version == null) version = myManager.getMinecraftVersion();
                requestMap.get(version).addOverride(entry.getValue().getKey(), entry.getValue().getValue());
                if (!enabled) {
                    AssetResourcePack.textureOverrides.add("assets/minecraft/textures/" + entry.getValue().getValue());
                }
            }
        }
        if (!enabled && !langOverrides.isEmpty()) {
            LangOverridePack.langOverrides.addAll(langOverrides);
        }
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

        private final List<Pair<MCVersion, String>> textures = new ArrayList<>();
        private final List<Pair<MCVersion, Pair<String, String>>> textureOverrides = new ArrayList<>();
        private final List<Pair<MCVersion, String>> sounds = new ArrayList<>();
        private final List<Pair<MCVersion, String>> structures = new ArrayList<>();
        private final List<String> langOverrides = new ArrayList<>();

        private Builder(String name, FeatureManager manager, Supplier<Boolean> enabledTest) {
            this.name = name;
            this.manager = manager;
            this.enabledTest = enabledTest;
        }

        /** Use this when you need to set a different MC version to fetch the assets from. */
        public Builder addTextures(MCVersion version, String... textures) {
            for (String texture : textures) {
                if (!texture.endsWith(".png") && !texture.endsWith(".mcmeta")) {
                    texture += ".png";
                }
                this.textures.add(Pair.of(version, texture));
            }
            return this;
        }

        public Builder addTextures(String... textures) {
            return addTextures(null, textures);
        }

        public Builder addTextureOverride(MCVersion version, String fromName, String toName) {
            if (!fromName.endsWith(".png") && !fromName.endsWith(".mcmeta")) {
                fromName += ".png";
            }
            if (!toName.endsWith(".png") && !toName.endsWith(".mcmeta")) {
                toName += ".png";
            }
            this.textureOverrides.add(Pair.of(version, Pair.of(fromName, toName)));
            return this;
        }

        public Builder addTextureOverride(String fromName, String toName) {
            return addTextureOverride(null, fromName, toName);
        }

        public Builder addTextureOverride(String name) {
            return addTextureOverride(null, name, name);
        }

        public Builder addLangOverrides(String... langKeys) {
            langOverrides.addAll(Arrays.asList(langKeys));
            return this;
        }

        /** Use this when you need to set a different MC version to fetch the assets from. */
        public Builder addSounds(MCVersion version, String... sounds) {
            for (String sound : sounds) {
                if (!sound.endsWith(".ogg")) {
                    sound += ".ogg";
                }
                this.sounds.add(Pair.of(version, sound));
            }
            return this;
        }

        public Builder addSounds(String... sounds) {
            return addSounds(null, sounds);
        }

        /** Use this when you need to set a different MC version to fetch the assets from. */
        public Builder addNumberedSounds(MCVersion version, String base, int start, int end) {
            for (int i = start; i <= end; i++) {
                this.sounds.add(Pair.of(version, base + i + ".ogg"));
            }
            return this;
        }

        public Builder addNumberedSounds(String base, int start, int end) {
            return addNumberedSounds(null, base, start, end);
        }

        /** Use this when you need to set a different MC version to fetch the assets from. */
        public Builder addStructures(MCVersion version, String... structures) {
            for (String structure : structures) {
                if (!structure.endsWith(".nbt")) {
                    structure += ".nbt";
                }
                this.structures.add(Pair.of(version, structure));
            }
            return this;
        }

        public Builder addStructures(String... structures) {
            return addStructures(null, structures);
        }

        public Feature build() {
            return new Feature(name, manager, enabledTest, textures, textureOverrides, sounds, structures, langOverrides);
        }
    }
}
