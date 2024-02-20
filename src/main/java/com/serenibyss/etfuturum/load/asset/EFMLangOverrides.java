package com.serenibyss.etfuturum.load.asset;

import com.serenibyss.etfuturum.load.feature.Feature;

import java.util.Set;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMLangOverrides {

    public static void init(Set<String> s) {
        addOverride(MISC.newSlabs, "tile.stoneSlab.name", s);
        addOverride(MISC.newSlabs, "tile.stoneSlab.stone.name", s);
    }

    private static void addOverride(Feature feature, String langKey, Set<String> s) {
        if (!feature.isEnabled()) {
            // this is an exclusionary list, so only add to list if the feature is *not* enabled
            s.add(langKey);
        }
    }
}
