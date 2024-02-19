package com.serenibyss.etfuturum.stats;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.Nullable;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMStatList {

    public static StatBase TIME_SINCE_REST;
    public static StatBase INTERACT_WITH_STONECUTTER;

    public static void init() {
        TIME_SINCE_REST = createBasicStat(MC13.phantom, "stat.timeSinceRest", StatBase.timeStatType);
        INTERACT_WITH_STONECUTTER = createBasicStat(MC14.stonecutter, "stat.interactWithStonecutter");
    }

    private static StatBasic createBasicStat(Feature feature, String id) {
        return createBasicStat(feature, id, null, false);
    }

    private static StatBasic createBasicStat(Feature feature, String id, boolean independent) {
        return createBasicStat(feature, id, null, independent);
    }

    private static StatBasic createBasicStat(Feature feature, String id, @Nullable IStatType type) {
        return createBasicStat(feature, id, type, false);
    }

    private static StatBasic createBasicStat(Feature feature, String id, @Nullable IStatType type, boolean independent) {
        if (feature == null || !feature.isEnabled()) {
            return null;
        }
        StatBasic stat;
        if (type == null) {
            stat = new StatBasic(id, new TextComponentTranslation(id));
        } else {
            stat = new StatBasic(id, new TextComponentTranslation(id), type);
        }
        if (independent) {
            stat.initIndependentStat();
        }
        stat.registerStat();
        return stat;
    }
}
