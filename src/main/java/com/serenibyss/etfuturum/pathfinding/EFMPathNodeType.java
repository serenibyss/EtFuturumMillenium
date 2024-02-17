package com.serenibyss.etfuturum.pathfinding;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraftforge.common.util.EnumHelper;

import static com.serenibyss.etfuturum.load.feature.Features.MC13;

public class EFMPathNodeType {

    public static PathNodeType WATER_BORDER;

    public static void init() {
        WATER_BORDER = addPathNodeType(MC13.turtle, "WATER_BORDER", 8.0F);
    }

    private static PathNodeType addPathNodeType(Feature feature, String name, float value) {
        if(!feature.isEnabled())
            return null;

        return EnumHelper.addEnum(PathNodeType.class, name, new Class[]{float.class}, value);
    }


}
