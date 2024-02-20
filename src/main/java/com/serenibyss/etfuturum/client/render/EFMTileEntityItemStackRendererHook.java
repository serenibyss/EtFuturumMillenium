package com.serenibyss.etfuturum.client.render;

import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.client.render.entity.model.ModelTrident;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EFMTileEntityItemStackRendererHook {

    private static final ResourceLocation resItemGlint = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private static final ModelTrident trident = new ModelTrident();
    private static final TileEntityConduit conduit = new TileEntityConduit();

    /**
     * Specify a custom renderer. Return true if successfully rendered.
     * This is called from the top of {@link TileEntityItemStackRenderer#renderByItem(ItemStack, float)}.
     */
    public static boolean getRenderByItem(ItemStack stack, float partialTicks) {
        if (stack.getItem() == EFMItems.TRIDENT.getItem()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModelTrident.TEXTURE_LOCATION);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            trident.renderer();
            if (stack.hasEffect()) {
                GlStateManager.color(0.5019608f, 0.2509804f, 0.8f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(resItemGlint);
                renderEffect(Minecraft.getMinecraft().getTextureManager(), trident::renderer, 1);
            }
            GlStateManager.popMatrix();
            return true;
        }

        if (stack.getItem() == EFMBlocks.CONDUIT.getItem()) {
            TileEntityRendererDispatcher.instance.render(conduit, 0, 0, 0, 0, partialTicks);
            return true;
        }
        return false;
    }

    /** Renders the effect glint on an item. */
    public static void renderEffect(TextureManager textureManagerIn, Runnable renderModelFunction, int scale) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        textureManagerIn.bindTexture(resItemGlint);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        float f = (float)((System.nanoTime() / 1000000L) % 3000L) / 3000.0F / (float) scale;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        renderModelFunction.run();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        float f1 = (float)((System.nanoTime() / 1000000L) % 4873L) / 4873.0F / (float) scale;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderModelFunction.run();
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        textureManagerIn.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
}
