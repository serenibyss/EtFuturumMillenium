package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.client.render.entity.layers.LayerTropicalFishPattern;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTropicalFishA;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTropicalFishB;
import com.serenibyss.etfuturum.entities.passive.fish.EntityTropicalFish;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RenderTropicalFish extends RenderLiving<EntityTropicalFish> {
    private final ModelTropicalFishA modelFishA = new ModelTropicalFishA();
    private final ModelTropicalFishB modelFishB = new ModelTropicalFishB();
    public RenderTropicalFish(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelTropicalFishA(), 0.15f);
        this.addLayer(new LayerTropicalFishPattern(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTropicalFish entity) {
        return entity.getBodyTexture();
    }

    @Override
    public void doRender(EntityTropicalFish entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.mainModel = (ModelBase)(entity.getBaseVariant() == 0 ? this.modelFishA : this.modelFishB);
        float[] floats = entity.getBodyColorTexture();
        GlStateManager.color(floats[0], floats[1], floats[2]);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
