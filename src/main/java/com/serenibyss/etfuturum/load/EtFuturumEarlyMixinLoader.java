package com.serenibyss.etfuturum.load;

import com.google.common.collect.ImmutableList;
import com.serenibyss.etfuturum.load.asset.AssetDummyMod;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("EtFuturumMilleniumLoadingPlugin")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions("com.serenibyss.etfuturum.load.")
@IFMLLoadingPlugin.SortingIndex(1001)
public class EtFuturumEarlyMixinLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return "com.serenibyss.etfuturum.load.asset.AssetDummyMod";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public List<String> getMixinConfigs() {
        return ImmutableList.of("mixins.etfuturum.json");
    }
}
