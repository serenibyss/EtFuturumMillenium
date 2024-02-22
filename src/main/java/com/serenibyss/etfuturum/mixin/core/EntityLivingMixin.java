package com.serenibyss.etfuturum.mixin.core;

import com.serenibyss.etfuturum.entities.ai.EFMEntityAI;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public class EntityLivingMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void etfuturum$injectAI(World world, CallbackInfo ci) {
        EFMEntityAI.injectEntityAI((EntityLiving) (Object) this);
    }
}
