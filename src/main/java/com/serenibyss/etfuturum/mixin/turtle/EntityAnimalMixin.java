package com.serenibyss.etfuturum.mixin.turtle;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAnimal.class)
public class EntityAnimalMixin {


    @Shadow private int inLove;

    @Unique
    protected boolean canBreed() {
        return this.inLove <= 0;
    }

    @Inject(method = "processInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/EntityAnimal;getGrowingAge()I", ordinal = 0))
    private void etfuturum$addCanBreedOverride(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        if(!canBreed()) {
            cir.setReturnValue(false);
        }
    }

}
