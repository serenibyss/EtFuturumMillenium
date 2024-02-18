package com.serenibyss.etfuturum.advancement.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.serenibyss.etfuturum.advancement.base.AbstractCriterion;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;

public class TridentChannelingCriterion extends AbstractCriterion {

    private EntityPredicate[] entityPredicates = { EntityPredicate.ANY };

    @Override
    public void deserialize(JsonObject json, JsonDeserializationContext context) {
        JsonElement element = json.get("victims");
        if (element != null && !element.isJsonNull()) {
            JsonArray array = JsonUtils.getJsonArray(element, "entities");
            EntityPredicate[] predicates = new EntityPredicate[array.size()];

            for (int i = 0; i < array.size(); i++) {
                predicates[i] = EntityPredicate.deserialize(array.get(i));
            }
            entityPredicates = predicates;
        }
    }

    @Override
    public boolean test(EntityPlayerMP player, Entity entity) {
        for (EntityPredicate predicate : entityPredicates) {
            if (predicate.test(player, entity)) {
                return true;
            }
        }
        return false;
    }
}
