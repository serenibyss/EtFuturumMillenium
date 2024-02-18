package com.serenibyss.etfuturum.client.render.entity.model;

import com.serenibyss.etfuturum.EFMTags;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelTrident extends ModelBase {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(EFMTags.MODID, "textures/entity/trident.png");
    private final ModelRenderer modelRenderer;

    public ModelTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.modelRenderer = new ModelRenderer(this, 0, 0);
        this.modelRenderer.addBox(-0.5F, -4.0F, -0.5F, 1, 31, 1, 0.0F);
        ModelRenderer lvt_1_1_ = new ModelRenderer(this, 4, 0);
        lvt_1_1_.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1);
        this.modelRenderer.addChild(lvt_1_1_);
        ModelRenderer lvt_2_1_ = new ModelRenderer(this, 4, 3);
        lvt_2_1_.addBox(-2.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(lvt_2_1_);
        ModelRenderer lvt_3_1_ = new ModelRenderer(this, 4, 3);
        lvt_3_1_.mirror = true;
        lvt_3_1_.addBox(1.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(lvt_3_1_);
    }

    public void renderer() {
        this.modelRenderer.render(0.0625F);
    }
}
