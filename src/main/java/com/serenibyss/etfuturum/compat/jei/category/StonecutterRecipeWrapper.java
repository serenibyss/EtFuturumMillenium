package com.serenibyss.etfuturum.compat.jei.category;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class StonecutterRecipeWrapper implements IRecipeWrapper {

    protected final ItemStack inputStack;
    protected final ItemStack outputStack;

    public StonecutterRecipeWrapper(ItemStack inputStack, ItemStack outputStack) {
        this.inputStack = inputStack;
        this.outputStack = outputStack;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, inputStack);
        ingredients.setOutput(VanillaTypes.ITEM, outputStack);
    }
}
