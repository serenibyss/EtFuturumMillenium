package com.serenibyss.etfuturum.mixin.trident;

import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@ClientMixin
@Mixin(RenderItem.class)
public class RenderItemMixin {

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;getItemModelWithOverrides(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;"))
    public IBakedModel etfuturum$renderTrident(RenderItem renderItem, ItemStack stack, World world, EntityLivingBase entity) {
        if (stack.getItem() == EFMItems.TRIDENT.getItem()) {
            return renderItem.getItemModelMesher().getModelManager().getModel(new ModelResourceLocation("etfuturum:trident_in_hand#inventory"));
        }
        return renderItem.getItemModelWithOverrides(stack, world, entity);
    }
}
