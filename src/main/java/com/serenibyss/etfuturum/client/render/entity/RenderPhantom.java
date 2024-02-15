package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.layers.LayerPhantomEyes;
import com.serenibyss.etfuturum.client.render.entity.model.ModelPhantom;
import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RenderPhantom extends RenderLiving<EntityPhantom> {
    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation(EFMTags.MODID, "textures/entity/phantom.png");

    public RenderPhantom(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPhantom(), 0.75f);
        this.addLayer(new LayerPhantomEyes(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPhantom entity) {
        return PHANTOM_LOCATION;
    }

    @Override
    protected void preRenderCallback(EntityPhantom entitylivingbaseIn, float partialTickTime) {
        float scale = 0.15f + 1.0f;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(0.0F, 1.3125F, 0.1875F);
    }

    @Override
    protected void applyRotations(EntityPhantom entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        GlStateManager.rotate(entityLiving.rotationPitch, 1.0f, 0.0f, 0.0f);
    }
}
