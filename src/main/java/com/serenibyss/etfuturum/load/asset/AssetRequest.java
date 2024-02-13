package com.serenibyss.etfuturum.load.asset;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.serenibyss.etfuturum.load.feature.MCVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public class AssetRequest {

    private final MCVersion mcVersion;
    private final EnumMap<AssetType, Set<String>> assetFiles = new EnumMap<>(AssetType.class);

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

    public void send() {
        for (AssetType type : AssetType.VALUES) {
            String typeName = type.name().toLowerCase();
            Set<String> assets = assetFiles.get(type);
            if (assets == null || assets.isEmpty()) {
                continue;
            }

            Map<String, String> assetMapping = new HashMap<>();
            for (String asset : assets) {
                assetMapping.put("assets/minecraft/" + typeName + "/" + asset, "assets/etfuturum/" + typeName + "/" + asset);
            }
            AssetMoverAPI.fromMinecraft(mcVersion.getLatestVersion(), assetMapping);
        }
    }
}
