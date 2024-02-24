package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelCod;
import com.serenibyss.etfuturum.entities.passive.fish.EntityCod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class RenderCod extends RenderLiving<EntityCod> {
    private static final ResourceLocation COD_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/fish/cod.png");

    public RenderCod(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelCod(), 0.2f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCod entity) {
        return COD_TEXTURE;
    }

    @Override
    protected void applyRotations(EntityCod entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6f * ageInTicks);
        GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
        if(!entityLiving.isInWater()) {
            GlStateManager.translate(0.1f, 0.1f, -0.1f);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        }
    }
}
