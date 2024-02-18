package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTrident;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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
        Entity entity = trident.shootingEntity;
        if (entity != null && trident.isNoPhysics()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = lerp(partialTicks * 0.5F, entity.rotationYaw, entity.prevRotationYaw) * ((float) Math.PI / 180F);
            double d1 = Math.cos(d0);
            double d2 = Math.sin(d0);
            double d3 = lerp(partialTicks, entity.prevPosX, entity.posX);
            double d4 = lerp(partialTicks, entity.prevPosY + (double)entity.getEyeHeight() * 0.8D, entity.posY + (double)entity.getEyeHeight() * 0.8D);
            double d5 = lerp(partialTicks, entity.prevPosZ, entity.posZ);
            double d6 = d1 - d2;
            double d7 = d2 + d1;
            double d8 = lerp(partialTicks, trident.prevPosX, trident.posX);
            double d9 = lerp(partialTicks, trident.prevPosY, trident.posY);
            double d10 = lerp(partialTicks, trident.prevPosZ, trident.posZ);
            double d11 = (float) (d3 - d8);
            double d12 = (float) (d4 - d9);
            double d13 = (float) (d5 - d10);
            double d14 = Math.sqrt(d11 * d11 + d12 * d12 + d13 * d13);
            int i = trident.getEntityId() + trident.ticksExisted;
            double d15 = (double)((float)i + partialTicks) * -0.1D;
            double d16 = Math.min(0.5D, d14 / 30.0D);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 255.0F, 255.0F);
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            int k = 7 - i % 7;

            for(int l = 0; l <= 37; ++l) {
                double d18 = (double)l / 37.0D;
                float f = 1.0F - (float)((l + k) % 7) / 7.0F;
                double d19 = d18 * 2.0D - 1.0D;
                d19 = (1.0D - d19 * d19) * d16;
                double d20 = x + d11 * d18 + Math.sin(d18 * Math.PI * 8.0D + d15) * d6 * d19;
                double d21 = y + d12 * d18 + Math.cos(d18 * Math.PI * 8.0D + d15) * 0.02D + (0.1D + d19) * 1.0D;
                double d22 = z + d13 * d18 + Math.sin(d18 * Math.PI * 8.0D + d15) * d7 * d19;
                float f1 = 0.87F * f + 0.3F * (1.0F - f);
                float f2 = 0.91F * f + 0.6F * (1.0F - f);
                float f3 = 0.85F * f + 0.5F * (1.0F - f);
                bufferbuilder.pos(d20, d21, d22).color(f1, f2, f3, 1.0F).endVertex();
                bufferbuilder.pos(d20 + 0.1D * d19, d21 + 0.1D * d19, d22).color(f1, f2, f3, 1.0F).endVertex();
                if (l > trident.returningTicks * 2) {
                    break;
                }
            }

            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(int i1 = 0; i1 <= 37; ++i1) {
                double d23 = (double)i1 / 37.0D;
                float f4 = 1.0F - (float)((i1 + k) % 7) / 7.0F;
                double d24 = d23 * 2.0D - 1.0D;
                d24 = (1.0D - d24 * d24) * d16;
                double d25 = x + d11 * d23 + Math.sin(d23 * Math.PI * 8.0D + d15) * d6 * d24;
                double d26 = y + d12 * d23 + Math.cos(d23 * Math.PI * 8.0D + d15) * 0.01D + (0.1D + d24) * 1.0D;
                double d27 = z + d13 * d23 + Math.sin(d23 * Math.PI * 8.0D + d15) * d7 * d24;
                float f5 = 0.87F * f4 + 0.3F * (1.0F - f4);
                float f6 = 0.91F * f4 + 0.6F * (1.0F - f4);
                float f7 = 0.85F * f4 + 0.5F * (1.0F - f4);
                bufferbuilder.pos(d25, d26, d27).color(f5, f6, f7, 1.0F).endVertex();
                bufferbuilder.pos(d25 + 0.1D * d24, d26, d27 + 0.1D * d24).color(f5, f6, f7, 1.0F).endVertex();
                if (i1 > trident.returningTicks * 2) {
                    break;
                }
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return TRIDENT_LOCATION;
    }

    private static double lerp(double d1, double d2, double d3) {
        return d2 + d1 * (d3 - d2);
    }
}
