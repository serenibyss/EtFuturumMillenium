package com.serenibyss.etfuturum.advancement.base;

import net.minecraft.advancements.ICriterionTrigger.Listener;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

class AdvancementListeners<T extends IAdvancementCriterion> {

    private final PlayerAdvancements playerAdvancements;
    private final Set<Listener<T>> listeners = new HashSet<>();

    protected AdvancementListeners(PlayerAdvancements playerAdvancements) {
        this.playerAdvancements = playerAdvancements;
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void add(Listener<T> listener) {
        listeners.add(listener);
    }

    public void remove(Listener<T> listener) {
        listeners.remove(listener);
    }

    private void grantCriterion(List<Listener<T>> listeners) {
        if (!listeners.isEmpty()) {
            for (Listener<T> listener : listeners) {
                listener.grantCriterion(playerAdvancements);
            }
        }
    }

    public void trigger(EntityPlayerMP player) {
        List<Listener<T>> list = new ArrayList<>();
        for (Listener<T> listener : listeners) {
            if (listener.getCriterionInstance().test(player)) {
                list.add(listener);
            }
        }
        grantCriterion(list);
    }

    public void trigger(EntityPlayerMP player, Entity entity) {
        List<Listener<T>> list = new ArrayList<>();
        for (Listener<T> listener : listeners) {
            if (listener.getCriterionInstance().test(player, entity)) {
                list.add(listener);
            }
        }
        grantCriterion(list);
    }
}
