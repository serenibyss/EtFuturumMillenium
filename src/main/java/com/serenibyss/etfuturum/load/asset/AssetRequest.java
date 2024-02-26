package com.serenibyss.etfuturum.load.asset;

import com.serenibyss.etfuturum.load.feature.MCVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public class AssetRequest {

    protected final MCVersion mcVersion;
    protected final EnumMap<AssetType, Set<String>> assetFiles = new EnumMap<>(AssetType.class);
    protected final Map<String, String> assetOverrides = new HashMap<>();

    public AssetRequest(MCVersion mcVersion) {
        this.mcVersion = mcVersion;
    }

    public void add(AssetType type, String entry) {
        assetFiles.computeIfAbsent(type, $ -> new HashSet<>()).add(entry);
    }

    public void addOverride(String fromName, String toName) {
        assetOverrides.put(fromName, toName);
    }

    public boolean isEmpty() {
        return assetFiles.isEmpty();
    }

    protected MCVersion getMinecraftVersion() {
        return mcVersion;
    }

    protected Map<String, String> getAssetRequest() {
        Map<String, String> assetRequest = new HashMap<>();
        for (AssetType type : AssetType.VALUES) {
            String typeName = type.name().toLowerCase();
            Set<String> assets = assetFiles.get(type);
            if (assets == null || assets.isEmpty()) {
                continue;
            }

            for (String asset : assets) {
                assetRequest.put("assets/minecraft/" + typeName + "/" + asset, "assets/etfuturum/" + typeName + "/" + asset);
            }
        }
        for (var override : assetOverrides.entrySet()) {
            assetRequest.put("assets/minecraft/textures/" + override.getKey(), "assets/minecraft/textures/" + override.getValue());
        }
        return assetRequest;
    }
}
