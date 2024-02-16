package com.serenibyss.etfuturum.mixin.core;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Advancement.Builder.class)
public interface AdvancementBuilderAccessor {

    @Accessor
    ResourceLocation getParentId();

    @Accessor
    Advancement getParent();

    @Accessor("parent")
    void setParent(Advancement parent);

    @Accessor
    DisplayInfo getDisplay();

    @Accessor
    AdvancementRewards getRewards();

    @Accessor
    Map<String, Criterion> getCriteria();

    @Accessor
    String[][] getRequirements();
}
