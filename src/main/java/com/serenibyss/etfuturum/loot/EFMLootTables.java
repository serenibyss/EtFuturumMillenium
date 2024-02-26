package com.serenibyss.etfuturum.loot;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMLootTables {

    public static ResourceLocation ENTITIES_PHANTOM;
    public static ResourceLocation ENTITIES_COD;
    public static ResourceLocation ENTITIES_SALMON;
    public static ResourceLocation ENTITIES_PUFFERFISH;
    public static ResourceLocation ENTITIES_TROPICAL_FISH;

    public static void init() {
        ENTITIES_PHANTOM = register(MC13.phantom, "entities/phantom");
        ENTITIES_COD = register(MC13.fish, "entities/cod");
        ENTITIES_SALMON = register(MC13.fish, "entities/salmon");
        ENTITIES_PUFFERFISH = register(MC13.fish, "entities/pufferfish");
        ENTITIES_TROPICAL_FISH = register(MC13.fish, "entities/tropical_fish");
    }

    private static ResourceLocation register(Feature feature, String name) {
        if (!feature.isEnabled()) {
            return null;
        }
        ResourceLocation loc = new ResourceLocation(EFMTags.MODID, name);
        return LootTableList.register(loc);
    }
}
