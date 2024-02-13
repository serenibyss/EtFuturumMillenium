package com.serenibyss.etfuturum.load.feature;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.relauncher.Side;

// this is the class you are looking for
public final class Features {

    public static final Features13 MC13 = new Features13();
    public static final Features14 MC14 = new Features14();
    public static final Features15 MC15 = new Features15();
    public static final Features16 MC16 = new Features16();
    public static final Features17 MC17 = new Features17();
    public static final Features18 MC18 = new Features18();
    public static final Features19 MC19 = new Features19();
    public static final Features20 MC20 = new Features20();

    public static void init(FMLConstructionEvent event) {
        if (event.getSide() == Side.CLIENT) {
            FeatureManager.gatherAssets();
        }
    }
}
