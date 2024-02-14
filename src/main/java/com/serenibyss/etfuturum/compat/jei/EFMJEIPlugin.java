package com.serenibyss.etfuturum.compat.jei;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.api.StonecutterRegistry;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.compat.jei.category.StonecutterCategory;
import com.serenibyss.etfuturum.compat.jei.category.StonecutterRecipeWrapper;
import com.serenibyss.etfuturum.compat.jei.category.StonecutterTransferInfo;
import com.serenibyss.etfuturum.load.feature.Features;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@JEIPlugin
public class EFMJEIPlugin implements IModPlugin {

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(EFMTags.MODID, "textures/gui/jei_gui_vanilla.png");

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (Features.MC14.stonecutter.isEnabled()) {
            registry.addRecipeCategories(new StonecutterCategory(registry.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public void register(@NotNull IModRegistry registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        if (Features.MC14.stonecutter.isEnabled()) {
            registry.addRecipes(StonecutterRegistry.instance().getAllRecipes().entrySet().stream()
                    .flatMap(e -> e.getValue().stream()
                            .map(v -> new StonecutterRecipeWrapper(e.getKey(), v)))
                    .collect(Collectors.toList()), StonecutterCategory.UID);
            registry.addRecipeCatalyst(EFMBlocks.STONECUTTER.getItemStack(), StonecutterCategory.UID);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new StonecutterTransferInfo());
        }
    }
}
