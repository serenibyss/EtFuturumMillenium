package com.serenibyss.etfuturum.client.render.entity;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTurtle;
import com.serenibyss.etfuturum.entities.passive.EntityTurtle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RenderTurtle extends RenderLiving<EntityTurtle> {
    private static final ResourceLocation TURTLE_LOCATION = new ResourceLocation(EFMTags.MODID, "textures/entity/turtle/big_sea_turtle.png");

    public RenderTurtle(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelTurtle(0.0F), 0.35f);
    }

    @Override
    public void doRender(EntityTurtle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity.isChild()) {
            this.shadowSize *= 0.5f;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTurtle entity) {
        return TURTLE_LOCATION;
    }
}
