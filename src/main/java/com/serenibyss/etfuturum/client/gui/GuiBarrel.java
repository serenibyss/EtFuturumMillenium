package com.serenibyss.etfuturum.client.gui;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.gui.base.EFMGuiContainer;
import com.serenibyss.etfuturum.containers.ContainerBarrel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBarrel extends EFMGuiContainer<ContainerBarrel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/gui/container/shulker_box.png");
    private final IInventory playerInv;

    public GuiBarrel(IInventory playerInv, IInventory inventory, EntityPlayer player) {
        super(new ContainerBarrel(playerInv, inventory, player));
        this.playerInv = playerInv;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(getTitle().getUnformattedText(), 8, 6, 4210752);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }
}
