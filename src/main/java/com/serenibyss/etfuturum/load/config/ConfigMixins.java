package com.serenibyss.etfuturum.load.config;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.EtFuturumConfigLoader;
import net.minecraftforge.common.config.Config;

// todo collapse into others?
@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/Mixins")
public class ConfigMixins {

    // 1.13: Update Aquatic

    // 1.14: Village & Pillage

    // 1.15: Buzzy Bees

    // 1.16: Nether Update

    // 1.17: Caves & Cliffs Part I

    // 1.18: Caves & Cliffs Part II

    // 1.19: The Wild Update

    // 1.20: Trails & Tales


    // MUST be at the bottom!
    static {
        EtFuturumConfigLoader.register(ConfigMixins.class);
    }
}
