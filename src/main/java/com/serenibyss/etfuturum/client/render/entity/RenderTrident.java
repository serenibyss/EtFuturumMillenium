package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTrident;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RenderTrident extends Render<EntityTrident> {

    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation(EFMTags.MODID, "textures/entity/trident.png");
    private final ModelTrident tridentModel = new ModelTrident();

    public RenderTrident(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindTexture(TRIDENT_LOCATION);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0f, 0.0f, 0.0f, 1.0f);
        tridentModel.renderer();
        GlStateManager.popMatrix();
        this.renderHelix(entity, x, y, z, entityYaw, partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();
    }

    protected void renderHelix(EntityTrident trident, double x, double y, double z, float yaw, float partialTicks) {

    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return TRIDENT_LOCATION;
    }
}
