package com.serenibyss.etfuturum.advancement.base;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCriterion<T extends IAdvancementCriterion<T>> implements IAdvancementCriterion<T> {

    private ResourceLocation id = new ResourceLocation("MISSING");

    @NotNull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void setId(@NotNull ResourceLocation id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractCriterion{id=" + this.id + "}";
    }
}
