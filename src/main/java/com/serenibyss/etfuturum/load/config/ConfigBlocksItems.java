package com.serenibyss.etfuturum.load.config;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.EtFuturumConfigLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = EFMTags.MODID, name = EFMTags.MODID + "/BlocksAndItems")
public class ConfigBlocksItems {

    // 1.13: Update Aquatic

    public static boolean enableConduit = true;
    public static boolean enableTridents = true;

    // 1.14: Village & Pillage

    public static boolean enableBarrels = true;
    public static boolean enableStonecutter = true;

    // 1.15: Buzzy Bees

    // 1.16: Nether Update

    // 1.17: Caves & Cliffs Part I

    // 1.18: Caves & Cliffs Part II

    // 1.19: The Wild Update

    // 1.20: Trails & Tales

    // Misc: Too spread out or small to set to one specific version

    @Comment("Adds new Stair blocks. Included are: Stone Stairs")
    public static boolean enableNewStairs = true;


    // MUST be at the bottom!
    static {
        EtFuturumConfigLoader.register(ConfigBlocksItems.class);
    }
}
