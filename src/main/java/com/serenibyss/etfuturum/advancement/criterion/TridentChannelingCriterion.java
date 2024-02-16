package com.serenibyss.etfuturum.advancement.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.serenibyss.etfuturum.advancement.base.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TridentChannelingCriterion extends AbstractCriterion {

    private final List<ResourceLocation> entityNames = new ArrayList<>();

    @Override
    public void deserialize(JsonObject json, JsonDeserializationContext context) {
        JsonArray entityArray = json.getAsJsonArray("victims");
        if (entityArray == null) return; // no entities specified
        for (JsonElement element : entityArray) {
            ResourceLocation entityId = new ResourceLocation(JsonUtils.getString(element, "type"));
            entityNames.add(entityId);
        }
    }

    @Override
    public boolean test(EntityPlayerMP player, Collection<? extends Entity> entities) {
        if (entityNames.isEmpty()) {
            return true;
        }
        for (Entity entity : entities) {
            EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
            if (entityNames.contains(entry.getRegistryName())) {
                return true;
            }
        }
        return false;
    }
}
