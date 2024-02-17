package com.serenibyss.etfuturum.mixin.trident;

import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import com.serenibyss.etfuturum.load.enums.EFMBipedArmPose;
import com.serenibyss.etfuturum.load.enums.EFMEnumAction;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientMixin
@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {

    @Shadow public abstract ModelPlayer getMainModel();

    @Inject(method = "setModelVisibilities", at = @At("TAIL"))
    private void etfuturum$setTridentVisibility(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        ModelPlayer modelPlayer = this.getMainModel();
        ItemStack mainHand = clientPlayer.getHeldItemMainhand();
        ItemStack offHand = clientPlayer.getHeldItemOffhand();

        if (clientPlayer.getItemInUseCount() > 0) {
            if (!mainHand.isEmpty()) {
                EnumAction action = mainHand.getItemUseAction();
                if (action == EFMEnumAction.SPEAR) {
                    if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                        modelPlayer.rightArmPose = EFMBipedArmPose.THROW_SPEAR;
                    } else {
                        modelPlayer.leftArmPose = EFMBipedArmPose.THROW_SPEAR;
                    }
                }
            }

            if (!offHand.isEmpty()) {
                EnumAction action = offHand.getItemUseAction();
                if (action == EFMEnumAction.SPEAR) {
                    if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                        modelPlayer.leftArmPose = EFMBipedArmPose.THROW_SPEAR;
                    } else {
                        modelPlayer.rightArmPose = EFMBipedArmPose.THROW_SPEAR;
                    }
                }
            }
        }
    }
}
