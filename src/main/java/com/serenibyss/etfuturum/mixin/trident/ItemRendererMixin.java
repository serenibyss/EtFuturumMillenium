package com.serenibyss.etfuturum.mixin.trident;

import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import com.serenibyss.etfuturum.load.enums.EFMEnumAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientMixin
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    protected abstract void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_);

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V", shift = At.Shift.BEFORE, by = 1))
    public void etfuturum$renderTridentFirstPerson(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equippedProgress, CallbackInfo ci) {
        if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand) {
            if (stack.getItemUseAction() == EFMEnumAction.SPEAR) {
                EnumHandSide handSide = hand == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
                int k = handSide == EnumHandSide.RIGHT ? 1 : -1;
                transformSideFirstPerson(handSide, equippedProgress);
                GlStateManager.translate((float) k * -0.5f, 0.7f, 0.1f);
                GlStateManager.rotate(-55.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate((float) k * 35.3F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((float) k * -9.785F, 0.0F, 0.0F, 1.0F);
                float f7 = (float) stack.getMaxItemUseDuration() - ((float)this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
                float f11 = f7 / 10.0F;
                if (f11 > 1.0F) {
                    f11 = 1.0F;
                }

                if (f11 > 0.1F) {
                    float f14 = MathHelper.sin((f7 - 0.1F) * 1.3F);
                    float f17 = f11 - 0.1F;
                    float f19 = f14 * f17;
                    GlStateManager.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
                }

                GlStateManager.translate(0.0F, 0.0F, f11 * 0.2F);
                GlStateManager.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                GlStateManager.rotate((float) k * 45.0F, 0.0F, -1.0F, 0.0F);
            }
        }
    }
}
