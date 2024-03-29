package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraftforge.common.util.EnumHelper;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMEnumCreatureAttribute {

    public static final EnumCreatureAttribute WATER = addCreatureAttribute(CORE, "water");

    private static EnumCreatureAttribute addCreatureAttribute(Feature feature, String name) {
        if(!feature.isEnabled()) {
            return null;
        }

        return EnumHelper.addCreatureAttribute(name);
    }
}
