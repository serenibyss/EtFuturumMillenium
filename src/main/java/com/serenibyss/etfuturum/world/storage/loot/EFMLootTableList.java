package com.serenibyss.etfuturum.world.storage.loot;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class EFMLootTableList {

    public static ResourceLocation ENTITIES_PHANTOM;

    public static void initLootTables() {
        if(Features.MC13.phantom.isEnabled())
            ENTITIES_PHANTOM = LootTableList.register(new ResourceLocation(EFMTags.MODID, "entities/phantom"));
    }
}
