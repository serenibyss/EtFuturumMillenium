package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.item.EnumAction;
import net.minecraftforge.common.util.EnumHelper;

public class EFMEnumAction {

    public static EnumAction SPEAR;

    protected static void init() {
        // todo:onion
        //SPEAR = addAction(MC13.trident, "spear");
    }

    private static EnumAction addAction(Feature feature, String name) {
        if (!feature.isEnabled()) {
            return null;
        }
        return EnumHelper.addAction(name);
    }
}
