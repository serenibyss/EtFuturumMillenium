package com.serenibyss.etfuturum.advancement;

import com.serenibyss.etfuturum.advancement.base.AdvancementTrigger;
import com.serenibyss.etfuturum.advancement.base.IAdvancementCriterion;
import com.serenibyss.etfuturum.advancement.criterion.TridentChannelingCriterion;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

import static com.serenibyss.etfuturum.advancement.hacks.AdvancementHacks.*;
import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMAdvancements {

    public static AdvancementTrigger<?> TRIDENT_CHANNELING;

    public static void initTriggers() {
        // todo: onio
        //TRIDENT_CHANNELING = registerTrigger(MC13.trident, "channeled_lightning", new TridentChannelingCriterion());
    }

    public static void initAdvancementHacks(Map<ResourceLocation, Advancement.Builder> map) {
        addKilledTrigger(MC13.phantom, "minecraft:adventure/kill_a_mob", map, "etfuturum:phantom");
        addKilledTrigger(MC13.phantom, "minecraft:adventure/kill_all_mobs", map, "etfuturum:phantom");
    }

    private static <T extends IAdvancementCriterion> AdvancementTrigger<T> registerTrigger(Feature feature, String id, T criterion) {
        if (!feature.isEnabled()) {
            return null;
        }
        AdvancementTrigger<T> trigger = new AdvancementTrigger<>(id, criterion);
        criterion.setId(trigger.getId());
        CriteriaTriggers.register(trigger);
        return trigger;
    }
}
