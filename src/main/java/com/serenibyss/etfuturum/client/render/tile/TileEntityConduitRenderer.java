package com.serenibyss.etfuturum.client.render.tile;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityConduitRenderer extends TileEntitySpecialRenderer<TileEntityConduit> {

    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/base.png");
    private static final ResourceLocation CAGE_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/cage.png");
    private static final ResourceLocation WIND_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/wind.png");
    private static final ResourceLocation VERTICAL_WIND_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/wind_vertical.png");
    private static final ResourceLocation OPEN_EYE_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/open_eye.png");
    private static final ResourceLocation CLOSED_EYE_TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/entity/conduit/closed_eye.png");

    private final ShellModel shellModel = new ShellModel();
    private final CageModel cageModel = new CageModel();
    private final WindModel windModel = new WindModel();
    private final EyeModel eyeModel = new EyeModel();

    public void render(TileEntityConduit tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float f = (float)tile.ticksExisted + partialTicks;
        if (!tile.isActive()) {
            float f1 = tile.getActiveRotation(0.0F);
            this.bindTexture(BASE_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
            this.shellModel.render(0.0625F);
            GlStateManager.popMatrix();
        } else {
            float f3 = tile.getActiveRotation(partialTicks) * (180F / (float) Math.PI);
            float f2 = MathHelper.sin(f * 0.1F) / 2.0F + 0.5F;
            f2 = f2 * f2 + f2;
            this.bindTexture(CAGE_TEXTURE);
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.5F, (float) y + 0.3F + f2 * 0.2F, (float) z + 0.5F);
            GlStateManager.rotate(f3, 0.5F, 1.0F, 0.5F);
            this.cageModel.render(0.0625F);
            GlStateManager.popMatrix();
            int j = tile.ticksExisted / 3 % 22;
            this.windModel.setRenderIndex(j);
            int k = tile.ticksExisted / 66 % 3;
            switch (k) {
                case 0 -> {
                    this.bindTexture(WIND_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    GlStateManager.scale(0.875F, 0.875F, 0.875F);
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                }
                case 1 -> {
                    this.bindTexture(VERTICAL_WIND_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    GlStateManager.scale(0.875F, 0.875F, 0.875F);
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                }
                case 2 -> {
                    this.bindTexture(WIND_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                    GlStateManager.scale(0.875F, 0.875F, 0.875F);
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    this.windModel.render(0.0625F);
                    GlStateManager.popMatrix();
                }
            }

            if (tile.isEyeOpen()) {
                this.bindTexture(OPEN_EYE_TEXTURE);
            } else {
                this.bindTexture(CLOSED_EYE_TEXTURE);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.5F, (float) y + 0.3F + f2 * 0.2F, (float) z + 0.5F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.rotate(-rendererDispatcher.entityYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(rendererDispatcher.entityPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            this.eyeModel.render(0.083333336F);
            GlStateManager.popMatrix();
        }

        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
    }

    @SideOnly(Side.CLIENT)
    static class CageModel extends ModelBase {

        private final ModelRenderer renderer;

        public CageModel() {
            this.textureWidth = 32;
            this.textureHeight = 16;
            this.renderer = new ModelRenderer(this, 0, 0);
            this.renderer.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        }

        public void render(float p_217106_6_) {
            this.renderer.render(p_217106_6_);
        }
    }

    @SideOnly(Side.CLIENT)
    static class EyeModel extends ModelBase {

        private final ModelRenderer renderer;

        public EyeModel() {
            this.textureWidth = 8;
            this.textureHeight = 8;
            this.renderer = new ModelRenderer(this, 0, 0);
            this.renderer.addBox(-4.0F, -4.0F, 0.0F, 8, 8, 0, 0.01F);
        }

        public void render(float p_217107_6_) {
            this.renderer.render(p_217107_6_);
        }
    }

    @SideOnly(Side.CLIENT)
    static class ShellModel extends ModelBase {

        private final ModelRenderer renderer;

        public ShellModel() {
            this.textureWidth = 32;
            this.textureHeight = 16;
            this.renderer = new ModelRenderer(this, 0, 0);
            this.renderer.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        }

        public void render(float p_217108_6_) {
            this.renderer.render(p_217108_6_);
        }
    }

    @SideOnly(Side.CLIENT)
    static class WindModel extends ModelBase {

        private final ModelRenderer[] renderers = new ModelRenderer[22];
        private int index;

        public WindModel() {
            this.textureWidth = 64;
            this.textureHeight = 1024;
            for(int i = 0; i < 22; ++i) {
                this.renderers[i] = new ModelRenderer(this, 0, 32 * i);
                this.renderers[i].addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
            }
        }

        public void render(float p_217109_6_) {
            this.renderers[this.index].render(p_217109_6_);
        }

        public void setRenderIndex(int index) {
            this.index = index;
        }
    }
}
