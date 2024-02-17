package com.serenibyss.etfuturum.load.feature;

import com.serenibyss.etfuturum.load.asset.AssetMover;
import com.serenibyss.etfuturum.load.asset.AssetRequest;
import com.serenibyss.etfuturum.load.asset.AssetType;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FeatureManager {

    private static final Set<FeatureManager> FEATURE_MANAGERS = new HashSet<>();
    private static final Object2ObjectMap<String, Feature> FEATURE_LOOKUP = new Object2ObjectOpenHashMap<>();

    private final Set<Feature> featureSet = new HashSet<>();

    protected FeatureManager() {
        FEATURE_MANAGERS.add(this);
    }

    public abstract MCVersion getMinecraftVersion();

    protected void registerFeature(String name, Feature feature) {
        featureSet.add(feature);
        FEATURE_LOOKUP.put(name, feature);
    }

    private void addAllAssets(Map<MCVersion, AssetRequest> requestMap) {
        for (AssetType type : AssetType.VALUES) {
            for (Feature feature : featureSet) {
                feature.addAssets(type, requestMap);
            }
        }
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
        Map<MCVersion, AssetRequest> requests = new EnumMap<>(MCVersion.class);
        for (MCVersion version : MCVersion.values()) {
            requests.put(version, new AssetRequest(version));
        }

        for (FeatureManager manager : FEATURE_MANAGERS) {
            manager.addAllAssets(requests);
        }

        for (AssetRequest request : requests.values()) {
            AssetMover.queue(request);
        }
        AssetMover.flush();
    }

    public static Feature getFeatureByName(String name) {
        return FEATURE_LOOKUP.get(name);
    }

    public static final FeatureManager CORE_MANAGER = new FeatureManager() {

        @Override
        public MCVersion getMinecraftVersion() {
            return MCVersion.MC1_12;
        }
    };

    static {
        // make sure stuff is loaded for mixins...
        Features.init();
    }
}
