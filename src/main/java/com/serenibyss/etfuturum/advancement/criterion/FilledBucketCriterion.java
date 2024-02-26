package com.serenibyss.etfuturum.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.serenibyss.etfuturum.advancement.base.AbstractCriterion;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class FilledBucketCriterion extends AbstractCriterion<FilledBucketCriterion> {

    private ItemPredicate itemPredicate;

    @Override
    public FilledBucketCriterion deserialize(JsonObject json, JsonDeserializationContext context) {
        FilledBucketCriterion criterion = new FilledBucketCriterion();
        criterion.itemPredicate = ItemPredicate.deserialize(json.get("item"));
        return criterion;
    }

    @Override
    public boolean test(EntityPlayerMP player, ItemStack stack) {
        return itemPredicate == null || itemPredicate.test(stack);
    }
}
