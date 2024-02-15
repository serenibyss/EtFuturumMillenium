package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraftforge.common.util.EnumHelper;

import static com.serenibyss.etfuturum.load.feature.Features.MC13;

public class EFMEnumCreatureAttribute {

    public static EnumCreatureAttribute WATER;

    protected static void init() {
        WATER = addCreatureAttribute(MC13.trident, "water");
    }

    private static EnumCreatureAttribute addCreatureAttribute(Feature feature, String name) {
        if(!feature.isEnabled()) {
            return null;
        }

        return EnumHelper.addCreatureAttribute(name);
    }
}
