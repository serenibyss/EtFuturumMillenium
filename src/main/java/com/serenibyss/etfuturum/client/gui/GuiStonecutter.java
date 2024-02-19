package com.serenibyss.etfuturum.client.gui;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.client.gui.base.EFMGuiContainer;
import com.serenibyss.etfuturum.containers.ContainerStonecutter;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.List;

public class GuiStonecutter extends EFMGuiContainer<ContainerStonecutter> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(EFMTags.MODID, "textures/gui/container/stonecutter.png");

    private final IInventory playerInv;

    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public GuiStonecutter(EntityPlayer player, World world, BlockPos pos) {
        super(new ContainerStonecutter(player, world, pos));
        this.playerInv = player.inventory;
        getContainer().setInventoryUpdateListener(this::containerChanged);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(getTitle().getFormattedText(), 8, 4, 4210752);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        int scaledProgress = (int) (41 * scrollOffs);
        drawTexturedModalRect(guiLeft + 119, guiTop + 15 + scaledProgress, 176 + (canScroll() ? 0 : 12), 0, 12, 15);
        renderButtons(mouseX, mouseY, guiLeft + 52, guiTop + 14, startIndex + 12);
        renderRecipes(guiLeft + 52, guiTop + 14, startIndex + 12);
    }

    private void renderButtons(int mouseX, int mouseY, int left, int top, int recipeOffset) {
        for (int i = this.startIndex; i < recipeOffset && i < getContainer().getRecipeOutputs().size(); ++i) {
            int j = i - this.startIndex;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            int j1 = this.ySize;
            if (i == getContainer().getRecipeIndex()) {
                j1 += 18;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36;
            }
            drawTexturedModalRect(k, i1 - 1, 0, j1, 16, 18);
        }
    }

    private void renderRecipes(int left, int top, int recipeOffset) {
        RenderHelper.enableGUIStandardItemLighting();
        List<ItemStack> outputItems = getContainer().getRecipeOutputs();

        for (int i = this.startIndex; i < recipeOffset && i < outputItems.size(); i++) {
            int j = i - this.startIndex;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            mc.getRenderItem().renderItemAndEffectIntoGUI(outputItems.get(i), k, i1);
        }
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.guiLeft + 52;
            int j = this.guiTop + 14;
            int k = this.startIndex + 12;

            for(int l = this.startIndex; l < k; ++l) {
                int i1 = l - this.startIndex;
                double d0 = mouseX - (double) (i + i1 % 4 * 16);
                double d1 = mouseY - (double) (j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && clickMenuButton(mc.player, l)) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    handleButtonClick(getContainer().windowId, l);
                    return;
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
                this.scrolling = true;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.scrolling && canScroll()) {
            int i = guiTop + 14;
            int j = i + 54;
            this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getHiddenRows()) + 0.5D) * 4;
        } else {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, int wheelMovement) {
        if (this.canScroll()) {
            this.scrollOffs = (float) ((double) this.scrollOffs - wheelMovement / (double) getHiddenRows());
            this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) getHiddenRows()) + 0.5D) * 4;
        }
    }

    private boolean canScroll() {
        return this.displayRecipes && getContainer().getRecipeOutputs().size() > 12;
    }

    protected int getHiddenRows() {
        return (getContainer().getRecipeOutputs().size() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = getContainer().hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }
}
