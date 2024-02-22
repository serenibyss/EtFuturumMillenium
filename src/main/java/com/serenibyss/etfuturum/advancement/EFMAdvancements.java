package com.serenibyss.etfuturum.advancement;

import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.advancement.base.AdvancementTrigger;
import com.serenibyss.etfuturum.advancement.base.IAdvancementCriterion;
import com.serenibyss.etfuturum.advancement.criterion.TridentChannelingCriterion;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.Map;

import static com.serenibyss.etfuturum.advancement.hacks.AdvancementHacks.*;
import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMAdvancements {

    public static AdvancementTrigger<?> TRIDENT_CHANNELING;

    /** Register new Advancement Triggers, things to call in code to trigger an Advancement being granted. */
    public static void initTriggers() {
        TRIDENT_CHANNELING = registerTrigger(MC13.trident, "channeled_lightning", new TridentChannelingCriterion());
    }

    /** Registering entirely new Advancements into the Vanilla Advancement categories. */
    private static void initAdditions(Map<ResourceLocation, Advancement.Builder> map) {
        addAdvancement(MC13.trident, "adventure/throw_trident", map);
        addAdvancement(MC13.trident, "adventure/very_very_frightening", map);
    }

    /** Modifications to existing Vanilla Advancements. */
    private static void initHacks(Map<ResourceLocation, Advancement.Builder> map) {
        addKilledTrigger(MC13.phantom, "minecraft:adventure/kill_a_mob", map, "etfuturum:phantom");
        addKilledTrigger(MC13.phantom, "minecraft:adventure/kill_all_mobs", map, "etfuturum:phantom");

        addBredTrigger(MC13.turtle, "minecraft:husbandry/breed_an_animal", map, "etfuturum:turtle");
        addBredTrigger(MC13.turtle, "minecraft:husbandry/bred_all_animals", map, "etfuturum:turtle");
    }

    public static void initAdvancementHacks(Map<ResourceLocation, Advancement.Builder> map) {
        initAdditions(map);
        initHacks(map);
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

    private static void addAdvancement(Feature feature, String name, Map<ResourceLocation, Advancement.Builder> map) {
        if (!feature.isEnabled()) {
            return;
        }
        try (InputStream stream = EFMAdvancements.class.getResourceAsStream("/assets/minecraft/advancements/" + name + ".json")) {
            if (stream != null) {
                InputStreamReader reader = new InputStreamReader(stream);
                Advancement.Builder builder = JsonUtils.fromJson(AdvancementManager.GSON, reader, Advancement.Builder.class);
                map.put(new ResourceLocation(name), builder);
            }
        } catch (IOException e) {
            EtFuturum.LOGGER.error("Failed to add new built-in advancement '{}'", name);
        }
    }
}
