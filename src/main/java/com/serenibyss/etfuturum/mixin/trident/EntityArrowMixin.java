package com.serenibyss.etfuturum.mixin.trident;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.serenibyss.etfuturum.entities.projectile.EntityTrident.LOYALTY_LEVEL;

@Mixin(EntityArrow.class)
public class EntityArrowMixin {


    @Shadow public EntityArrow.PickupStatus pickupStatus;
    @Shadow
    private int ticksInGround;

    @ModifyConstant(method = "onUpdate()V", constant = @Constant(floatValue = 0.6F))
    private float etfuturum$changeSwimDrag(float drag) {
        if(((EntityArrow)(Object)this) instanceof EntityTrident trident) {
            drag = 0.99F;
        }
        return drag;
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/EntityArrow;setDead()V"))
    private void etfuturum$trydespawn(CallbackInfo ci) {
        if(((EntityArrow)(Object)this) instanceof EntityTrident trident) {
            ticksInGround--;
            int loyalLevel = trident.getDataManager().get(LOYALTY_LEVEL);
            if(this.pickupStatus != EntityArrow.PickupStatus.ALLOWED || loyalLevel <= 0) {
                ++ticksInGround;

                if (ticksInGround >= 1200) {
                    trident.setDead();
                }
            }
        }
    }

}
