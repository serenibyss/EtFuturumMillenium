package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.asset.AssetRequest;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FeatureManager {

    private static final Set<FeatureManager> FEATURE_MANAGERS = new HashSet<>();

    private final Set<Feature> featureSet = new HashSet<>();

    protected FeatureManager() {
        FEATURE_MANAGERS.add(this);
    }

    public abstract MCVersion getMinecraftVersion();

    protected void registerFeature(Feature feature) {
        featureSet.add(feature);
    }

    // todo cache this if it ends up needing to be called often
    public static Set<Feature> getAllLoadedFeatures() {
        Set<Feature> features = new HashSet<>();
        for (FeatureManager manager : FEATURE_MANAGERS) {
            features.addAll(manager.featureSet.stream().filter(Feature::isEnabled).collect(Collectors.toSet()));
        }
        return features;
    }

    @SideOnly(Side.CLIENT)
    public static void gatherAssets() {
        Set<AssetRequest> requests = new HashSet<>();
        for (FeatureManager manager : FEATURE_MANAGERS) {
            AssetRequest request = new AssetRequest(manager.getMinecraftVersion());
            for (Feature feature : manager.featureSet) {
                request.addTextures(feature.textures);
                feature.textures.clear();

                request.addSounds(feature.sounds);
                feature.sounds.clear();
            }
            request.send();
        }
    }
}
