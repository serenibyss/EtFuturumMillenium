package com.serenibyss.etfuturum.stats;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMStatList {

    public static StatBase TIME_SINCE_REST;

    public static void init() {
        TIME_SINCE_REST = createBasicStat(MC13.phantom, "stat.timeSinceRest", StatBase.timeStatType);
    }

    private static StatBasic createBasicStat(Feature feature, String id, IStatType type) {
        return createBasicStat(feature, id, type, false);
    }

    private static StatBasic createBasicStat(Feature feature, String id, IStatType type, boolean independent) {
        if (feature == null || !feature.isEnabled()) {
            return null;
        }
        StatBasic stat = new StatBasic(id, new TextComponentTranslation(id), type);
        if (independent) {
            stat.initIndependentStat();
        }
        stat.registerStat();
        return stat;
    }
}
