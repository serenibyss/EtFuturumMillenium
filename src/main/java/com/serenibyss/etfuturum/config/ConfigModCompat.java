package com.serenibyss.etfuturum.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.serenibyss.etfuturum.EFMTags;
import net.minecraftforge.common.config.Config;

@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/ModCompat")
public class ConfigModCompat {

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
        ConfigAnytime.register(ConfigBlocksItems.class);
    }
}
