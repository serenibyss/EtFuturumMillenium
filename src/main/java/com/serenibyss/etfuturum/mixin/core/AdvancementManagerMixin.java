package com.serenibyss.etfuturum.mixin.core;

import com.serenibyss.etfuturum.advancement.EFMAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {

    @Shadow
    private boolean hasErrored;

    @Inject(method = "loadBuiltInAdvancements", at = @At(value = "RETURN"))
    private void etfuturum$editBuiltInAdvancements(Map<ResourceLocation, Advancement.Builder> map, CallbackInfo ci) {
        if (!hasErrored) {
            EFMAdvancements.initAdvancementHacks(map);
        }
    }
}
