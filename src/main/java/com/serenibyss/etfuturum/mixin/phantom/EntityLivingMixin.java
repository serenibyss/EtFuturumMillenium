package com.serenibyss.etfuturum.mixin.phantom;

import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityLiving.class)
abstract class EntityLivingMixin {

    @Shadow @Final private EntityLookHelper lookHelper;

    public EntityLivingMixin(World worldIn) {
        super();
    }

    @Redirect(method = "updateEntityActionState", at = @At(value = "INVOKE", args = "classValue=EntityPhantom", target = "Lnet/minecraft/entity/ai/EntityLookHelper;onUpdateLook()V"))
    public void etfuturum$disregardPhantomLookHelper(EntityLookHelper instance) {
        if(((EntityLiving)(Object)this) instanceof EntityPhantom phantom) {
            return;
        }
        this.lookHelper.onUpdateLook();
    }
}
