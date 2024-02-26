package com.serenibyss.etfuturum.client.render.entity.layers;

import com.serenibyss.etfuturum.client.render.entity.RenderTropicalFish;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTropicalFishA;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTropicalFishB;
import com.serenibyss.etfuturum.entities.passive.fish.EntityTropicalFish;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerTropicalFishPattern implements LayerRenderer<EntityTropicalFish> {
    private final RenderTropicalFish fishRenderer;
    private final ModelTropicalFishA modelA;
    private final ModelTropicalFishB modelB;

    public LayerTropicalFishPattern(RenderTropicalFish fishRenderer) {
        this.fishRenderer = fishRenderer;
        this.modelA = new ModelTropicalFishA(0.008f);
        this.modelB = new ModelTropicalFishB(0.008f);
    }

    @Override
    public void doRenderLayer(EntityTropicalFish entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!entitylivingbaseIn.isInvisible()) {
            ModelBase baseModel = (ModelBase) (entitylivingbaseIn.getBaseVariant() == 0 ? this.modelA : this.modelB);
            this.fishRenderer.bindTexture(entitylivingbaseIn.getPatternTexture());
            float[] patternColors = entitylivingbaseIn.getPatternColorTexture();
            GlStateManager.color(patternColors[0], patternColors[1], patternColors[2]);
            baseModel.setModelAttributes(this.fishRenderer.getMainModel());
            baseModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            baseModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
