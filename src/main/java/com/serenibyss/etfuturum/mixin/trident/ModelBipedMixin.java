package com.serenibyss.etfuturum.mixin.trident;

import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import com.serenibyss.etfuturum.load.enums.EFMBipedArmPose;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientMixin
@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin extends ModelBase {

    @Shadow
    private ModelBiped.ArmPose rightArmPose;

    @Shadow
    private ModelBiped.ArmPose leftArmPose;

    @Shadow
    private ModelRenderer bipedRightArm;

    @Shadow
    private ModelRenderer bipedLeftArm;

    @Shadow
    private ModelRenderer bipedBody;

    @Shadow
    private boolean isSneak;

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;copyModelAngles(Lnet/minecraft/client/model/ModelRenderer;Lnet/minecraft/client/model/ModelRenderer;)V", shift = At.Shift.BEFORE, by = 1))
    public void etfuturum$tridentArmPose(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (rightArmPose == EFMBipedArmPose.THROW_SPEAR) {
            bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5f - (float) Math.PI;
            bipedRightArm.rotateAngleY = 0.0f;
            if (swingProgress > 0) bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
            if (isSneak) bipedRightArm.rotateAngleX += 0.4f;
            bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }
        if (leftArmPose == EFMBipedArmPose.THROW_SPEAR) {
            if (rightArmPose != ModelBiped.ArmPose.BLOCK && rightArmPose != EFMBipedArmPose.THROW_SPEAR && rightArmPose != ModelBiped.ArmPose.BOW_AND_ARROW) {
                bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5f - (float) Math.PI;
                bipedLeftArm.rotateAngleY = 0.0f;
                if (swingProgress > 0) {
                    bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
                    // this warning is not correct
                    bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
                }
                if (isSneak) bipedLeftArm.rotateAngleX += 0.4f;
                bipedLeftArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            }
        }
    }
}
