package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelCod;
import com.serenibyss.etfuturum.client.render.entity.model.ModelSalmon;
import com.serenibyss.etfuturum.entities.passive.fish.EntityCod;
import com.serenibyss.etfuturum.entities.passive.fish.EntitySalmon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class RenderSalmon extends RenderLiving<EntitySalmon> {
    private static final ResourceLocation SALMON_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/fish/salmon.png");

    public RenderSalmon(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelSalmon(), 0.2f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntitySalmon entity) {
        return SALMON_TEXTURE;
    }

    @Override
    protected void applyRotations(EntitySalmon entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 1.0f;
        float f1 = 1.0f;
        if(!entityLiving.isInWater()) {
            f = 1.3f;
            f1 = 1.7f;
        }

        float f2 = f * 4.3F * MathHelper.sin(f1 * 0.6f * ageInTicks);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.1f, 0.1f, -0.4f);
        if(!entityLiving.isInWater()) {
            GlStateManager.translate(0.2f, 0.1f, 0.0f);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        }
    }
}
