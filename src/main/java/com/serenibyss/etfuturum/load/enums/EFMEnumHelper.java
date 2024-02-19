package com.serenibyss.etfuturum.load.enums;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EFMEnumHelper {

    public static void init() {
        EFMEnumAction.init();
        EFMEnumEnchantmentType.init();
        EFMEnumCreatureAttribute.init();
        EFMPathNodeType.init();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        EFMBipedArmPose.init();
    }
}
