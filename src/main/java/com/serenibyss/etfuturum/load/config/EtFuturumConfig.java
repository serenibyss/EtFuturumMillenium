package com.serenibyss.etfuturum.load.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.MCVersion;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

/** General configs, not tied to a specific feature addition. */
@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/EtFuturum")
public class EtFuturumConfig {

    @Comment({"Global config option to enable features only up to a specified version.", "Set to MC1_12 to disable everything.", "Default: MC1_20 (enable everything)"})
    public static MCVersion allFeaturesUpToVersion = MCVersion.MC1_20;

    // MUST be at the bottom!
    static {
        ConfigAnytime.register(EtFuturumConfig.class);
    }
}
