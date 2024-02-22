package com.serenibyss.etfuturum.load.enums;

public class EFMEnumHelper {

    public static void initEarlyEnums() {
        // Most be initialized before EntityLiving class is loaded as it holds an EnumMap of PathNodeType
        EFMPathNodeType.init();
    }
}
