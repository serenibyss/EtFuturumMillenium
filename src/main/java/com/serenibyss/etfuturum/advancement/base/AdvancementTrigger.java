package com.serenibyss.etfuturum.advancement.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.serenibyss.etfuturum.EFMTags;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdvancementTrigger<T extends IAdvancementCriterion> implements ICriterionTrigger<T> {

    private final ResourceLocation id;
    private final T criterion;
    private final Map<PlayerAdvancements, AdvancementListeners<T>> listeners = new HashMap<>();

    public AdvancementTrigger(@NotNull String name, @NotNull T criterion) {
        this.id = new ResourceLocation(EFMTags.MODID, name);
        this.criterion = criterion;
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void addListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<T> listener) {
        AdvancementListeners<T> efmListener = listeners.get(playerAdvancements);
        if (efmListener == null) {
            efmListener = new AdvancementListeners<>(playerAdvancements);
            listeners.put(playerAdvancements, efmListener);
        }
        efmListener.add(listener);
    }

    @Override
    public void removeListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<T> listener) {
        AdvancementListeners<T> efmListener = listeners.get(playerAdvancements);
        if (efmListener != null) {
            efmListener.remove(listener);
            if (efmListener.isEmpty()) {
                listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removeAllListeners(@NotNull PlayerAdvancements playerAdvancements) {
        listeners.remove(playerAdvancements);
    }

    @NotNull
    @Override
    public T deserializeInstance(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
        criterion.deserialize(json, context);
        return criterion;
    }

    /**
     * Trigger an advancement for a player unconditionally.
     *
     * @param player The player triggering the Advancement
     */
    public void trigger(@NotNull EntityPlayerMP player) {
        AdvancementListeners<T> listener = listeners.get(player.getAdvancements());
        if (listener != null) {
            listener.trigger(player);
        }
    }

    public void trigger(@NotNull EntityPlayerMP player, @NotNull Collection<? extends Entity> entities) {
        AdvancementListeners<T> listener = listeners.get(player.getAdvancements());
        if (listener != null) {
            listener.trigger(player, entities);
        }
    }
}
