package com.serenibyss.etfuturum.load.config;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.EtFuturumConfigLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/Entities")
public class ConfigEntities {

    // 1.13: Update Aquatic

    public static boolean enablePhantoms = true;

    @Comment({"How much damage Phantoms should do on attack. In 1.13, they dealt 6 damage. In 1.14+, they dealt 2 damage.", "Default: 2"})
    @RangeDouble(min = 2.0D, max = 2048.0D) // range from SharedMonsterAttributes.ATTACK_DAMAGE
    public static double phantomDamage = 2.0f;

    public static boolean enableTurtles = true;

    public static boolean enableFish = true;

    // 1.14: Village & Pillage

    // 1.15: Buzzy Bees

    // 1.16: Nether Update

    // 1.17: Caves & Cliffs Part I

    // 1.18: Caves & Cliffs Part II

    // 1.19: The Wild Update

    // 1.20: Trails & Tales


    // MUST be at the bottom!
    static {
        EtFuturumConfigLoader.register(ConfigEntities.class);
    }
}
