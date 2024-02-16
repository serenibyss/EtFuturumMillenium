package com.serenibyss.etfuturum.advancement.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public interface IAdvancementCriterion extends ICriterionInstance {

    void setId(ResourceLocation id);

    /** Override this if you need additional data out of the Advancement JSON. */
    default void deserialize(JsonObject json, JsonDeserializationContext context) {}


    // Implement AT LEAST ONE of these test functions in your criterion.

    default boolean test(EntityPlayerMP player) { throw new UnsupportedOperationException("Criteria for [player] not supported"); }
    default boolean test(EntityPlayerMP player, Collection<? extends Entity> entities) { throw new UnsupportedOperationException("Criteria for [player, entities] not supported"); };
}
