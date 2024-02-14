package com.serenibyss.etfuturum.compat.jei.category;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.compat.jei.EFMJEIPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StonecutterCategory implements IRecipeCategory<StonecutterRecipeWrapper> {

    public static final String UID = EFMTags.MODID + ":stonecutter";

    public static final int WIDTH = 82;
    public static final int HEIGHT = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public StonecutterCategory(IGuiHelper guiHelper) {
        ResourceLocation location = EFMJEIPlugin.RECIPE_GUI_VANILLA;
        background = guiHelper.createDrawable(location, 0, 220, WIDTH, HEIGHT);
        icon = guiHelper.createDrawableIngredient(EFMBlocks.STONECUTTER.getItemStack());
        localizedName = I18n.format("etfuturum.jei.category.stonecutter");
    }

    @NotNull
    @Override
    public String getUid() {
        return UID;
    }

    @NotNull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @NotNull
    @Override
    public String getModName() {
        return EFMTags.MODID;
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayout layout,
                          @NotNull StonecutterRecipeWrapper wrapper,
                          @NotNull IIngredients ingredients) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 0, 8);
        isg.init(1, false, 60, 8);
        isg.set(0, wrapper.inputStack);
        isg.set(1, wrapper.outputStack);
    }
}
