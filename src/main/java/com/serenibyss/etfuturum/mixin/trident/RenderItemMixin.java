package com.serenibyss.etfuturum.mixin.trident;

import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientMixin
@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow @Final private ItemModelMesher itemModelMesher;

    @Shadow protected abstract void renderItemModel(ItemStack stack, IBakedModel bakedmodel, TransformType transform, boolean leftHanded);

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V",
              at = @At(value = "HEAD"), cancellable = true)
    public void etfuturum$renderTrident(ItemStack stack, TransformType type, CallbackInfo ci) {
        boolean flag = type == TransformType.GUI || type == TransformType.GROUND || type == TransformType.FIXED;
        if (!stack.isEmpty() && stack.getItem() == EFMItems.TRIDENT.getItem() && !flag) {
            IBakedModel model = itemModelMesher.getModelManager().getModel(new ModelResourceLocation("minecraft:item/trident_in_hand#inventory"));
            renderItemModel(stack, model, type, false);
            ci.cancel();
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
            at = @At("HEAD"), cancellable = true)
    public void etfuturum$renderTrident(ItemStack stack, EntityLivingBase entity, TransformType type, boolean leftHanded, CallbackInfo ci) {
        boolean flag = type == TransformType.GUI || type == TransformType.GROUND || type == TransformType.FIXED;
        if (!stack.isEmpty() && entity != null && stack.getItem() == EFMItems.TRIDENT.getItem() && !flag) {
            IBakedModel model = itemModelMesher.getModelManager().getModel(new ModelResourceLocation("minecraft:item/trident_in_hand#inventory"));
            renderItemModel(stack, model, type, leftHanded);
            ci.cancel();
        }
    }
}
