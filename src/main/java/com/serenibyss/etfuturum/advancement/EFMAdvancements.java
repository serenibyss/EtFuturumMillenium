package com.serenibyss.etfuturum.advancement;

import com.serenibyss.etfuturum.advancement.base.AdvancementTrigger;
import com.serenibyss.etfuturum.advancement.base.IAdvancementCriterion;
import com.serenibyss.etfuturum.advancement.criterion.TridentChannelingCriterion;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.advancements.CriteriaTriggers;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMAdvancements {

    public static AdvancementTrigger<?> TRIDENT_CHANNELING;

    public static void initTriggers() {
        // todo: onion
        //TRIDENT_CHANNELING = registerTrigger(MC13.trident, "channeled_lightning", new TridentChannelingCriterion());
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
