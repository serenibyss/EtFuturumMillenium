package com.serenibyss.etfuturum.load.asset;

import com.serenibyss.etfuturum.load.feature.MCVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public class AssetRequest {

    protected final MCVersion mcVersion;
    protected final EnumMap<AssetType, Set<String>> assetFiles = new EnumMap<>(AssetType.class);

    public AssetRequest(MCVersion mcVersion) {
        this.mcVersion = mcVersion;
    }

    public void addTextures(Set<String> textures) {
        assetFiles.computeIfAbsent(AssetType.TEXTURES, $ -> new HashSet<>()).addAll(textures);
    }

    public void addSounds(Set<String> sounds) {
        assetFiles.computeIfAbsent(AssetType.SOUNDS, $ -> new HashSet<>()).addAll(sounds);
    }

    public void addStructures(Set<String> structures) {
        assetFiles.computeIfAbsent(AssetType.STRUCTURES, $ -> new HashSet<>()).addAll(structures);
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
        return assetRequest;
    }
}
