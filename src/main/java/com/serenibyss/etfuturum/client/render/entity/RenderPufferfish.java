package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelPufferfishLarge;
import com.serenibyss.etfuturum.client.render.entity.model.ModelPufferfishMedium;
import com.serenibyss.etfuturum.client.render.entity.model.ModelPufferfishSmall;
import com.serenibyss.etfuturum.entities.passive.fish.EntityPufferfish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class RenderPufferfish extends RenderLiving<EntityPufferfish> {
    private static final ResourceLocation PUFFER_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/fish/pufferfish.png");
    private int puffState;
    private final ModelPufferfishLarge largeModel = new ModelPufferfishLarge();
    private final ModelPufferfishMedium mediumModel = new ModelPufferfishMedium();
    private final ModelPufferfishSmall smallModel = new ModelPufferfishSmall();

    public RenderPufferfish(RenderManager manager) {
        super(manager, new ModelPufferfishLarge(), 0.1f);
        this.puffState = 3;
    }

    @Override
    public void doRender(EntityPufferfish entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int i = entity.getPuffState();
        if(i != puffState) {
            if(i == 0) {
                mainModel = smallModel;
            } else if(i == 1) {
                mainModel = mediumModel;
            } else {
                mainModel = largeModel;
            }
        }

        puffState = i;
        shadowSize = 0.1f + 0.1f * i;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void applyRotations(EntityPufferfish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.translate(0.0f, MathHelper.cos(ageInTicks * 0.05f) * 0.08f, 0.0f);
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPufferfish entity) {
        return PUFFER_TEXTURE;
    }
}
