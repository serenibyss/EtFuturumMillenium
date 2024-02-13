package com.serenibyss.etfuturum.load.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.serenibyss.etfuturum.EFMTags;
import net.minecraftforge.common.config.Config;

// todo rename?
@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/Functions")
public class ConfigFunctions {

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
        ConfigAnytime.register(ConfigFunctions.class);
    }
}
