package com.serenibyss.etfuturum.advancement.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IAdvancementCriterion<T extends IAdvancementCriterion<T>> extends ICriterionInstance {

    void setId(ResourceLocation id);

    /** Override this if you need additional data out of the Advancement JSON. Otherwise, just return this object. */
    T deserialize(JsonObject json, JsonDeserializationContext context);

    // Implement AT LEAST ONE of these test functions in your criterion.

    default boolean test(EntityPlayerMP player) { throw new UnsupportedOperationException("Criteria for [player] not supported"); }
    default boolean test(EntityPlayerMP player, Entity entity) { throw new UnsupportedOperationException("Criteria for [player, entity] not supported"); }
    default boolean test(EntityPlayerMP player, ItemStack stack) { throw new UnsupportedOperationException("Criteria for [player, itemstack] not supported"); }
}
