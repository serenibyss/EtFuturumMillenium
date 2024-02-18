package com.serenibyss.etfuturum.mixin.core;

import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.load.ClientProxy;
import com.serenibyss.etfuturum.load.annotation.ClientMixin;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ClientMixin
@Mixin(value = ModelLoader.class, remap = false)
public class ModelLoaderMixin {

    @Shadow
    @Final
    private Map<ModelResourceLocation, IModel> stateModels;

    @Inject(method = "setupModelRegistry", at = @At("HEAD"))
    public void etfuturum$injectModels(CallbackInfoReturnable<IRegistry<ModelResourceLocation, IBakedModel>> cir) {
        if (!FMLClientHandler.instance().hasError()) {
            List<String> topLevelModels = new ArrayList<>();
            ClientProxy.registerCustomItemModels(topLevelModels);
            for (String modelName : topLevelModels) {
                try {
                    IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(modelName));
                    stateModels.put(new ModelResourceLocation("minecraft:" + modelName + "#inventory"), model);
                } catch (Exception e) {
                    EtFuturum.LOGGER.error("Failed to load model " + modelName, e);
                }
            }
        }
    }
}
