package com.serenibyss.etfuturum.mixin.core;

import com.serenibyss.etfuturum.client.render.EFMTileEntityItemStackRendererHook;
import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientMixin
@Mixin(TileEntityItemStackRenderer.class)
public class TileEntityItemStackRendererMixin {

    @Inject(method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    public void etfuturum$renderCustomTESRItems(ItemStack stack, float partialTicks, CallbackInfo ci) {
        if (EFMTileEntityItemStackRendererHook.getRenderByItem(stack, partialTicks)) {
            ci.cancel();
        }
    }
}
