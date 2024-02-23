package com.serenibyss.etfuturum.recipes;

import com.serenibyss.etfuturum.EtFuturum;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unused")
public class RecipeHelpers {

    /**
     * Adds a smelting recipe. Gives 0.15f experience.
     */
    public static void addSmeltingRecipe(@NotNull ItemStack input, @NotNull ItemStack output) {
        addSmeltingRecipe(input, output, 0.15f);
    }

    public static void addSmeltingRecipe(@NotNull ItemStack input, @NotNull ItemStack output, float experience) {
        if (input.isEmpty() || output.isEmpty()) return;

        FurnaceRecipes recipes = FurnaceRecipes.instance();
        if (recipes.getSmeltingResult(input).isEmpty()) {
            recipes.addSmeltingRecipe(input, output, experience);
        } else {
            EtFuturum.LOGGER.warn("Tried to register duplicate Furnace Recipe: {}x {}:{} -> {}x {}:{}, {}exp",
                    input.getCount(), Objects.requireNonNull(input.getItem().getRegistryName()).getNamespace(), input.getDisplayName(),
                    output.getCount(), Objects.requireNonNull(output.getItem().getRegistryName()).getNamespace(), output.getDisplayName(),
                    experience);
        }
    }

    public static void addShapedRecipe(@NotNull String name, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapedRecipe(name, result, false, recipe);
    }

    public static void addMirroredShapedRecipe(@NotNull String name, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapedRecipe(name, result, true, recipe);
    }

    private static void addShapedRecipe(@NotNull String name, @NotNull ItemStack result, boolean isMirrored, @NotNull Object... recipe) {
        if (!validateItems(result) || !validateItems(recipe)) return;

        IRecipe shapedOreRecipe = new EFMShapedRecipe(result, recipe).setMirrored(isMirrored).setRegistryName(name);
        ForgeRegistries.RECIPES.register(shapedOreRecipe);
    }

    public static void addShapelessRecipe(@NotNull String name, @NotNull ItemStack result, @NotNull Object... recipe) {
        if (!validateItems(result) || !validateItems(recipe)) return;

        IRecipe shapelessOreRecipe = new EFMShapelessRecipe(result, recipe).setRegistryName(name);

        try {
            Field field = ShapelessOreRecipe.class.getDeclaredField("isSimple");
            field.setAccessible(true);
            field.setBoolean(shapelessOreRecipe, false);
        } catch (ReflectiveOperationException e) {
            EtFuturum.LOGGER.error("Failed to mark shapeless recipe as complex", e);
        }

        ForgeRegistries.RECIPES.register(shapelessOreRecipe);
    }

    public static void registerOre(String oreName, ItemStack ore) {
        if (validateItems(ore)) {
            OreDictionary.registerOre(oreName, ore);
        }
    }

    public static void registerOre(String oreName, Item ore) {
        if (validateItems(ore)) {
            OreDictionary.registerOre(oreName, ore);
        }
    }

    public static void registerOre(String oreName, Block ore) {
        if (validateItems(ore)) {
            OreDictionary.registerOre(oreName, ore);
        }
    }

    private static boolean validateItems(Object... objects) {
        for (Object object : objects) {
            if (object == null) return false;
            if (object == Blocks.AIR) return false;
            if (object == ItemStack.EMPTY) return false;
        }
        return true;
    }

    public static void removeFurnaceSmelting(@NotNull ItemStack input) {
        if (!validateItems(input)) return;

        FurnaceRecipes.instance().getSmeltingList().keySet()
                .removeIf(stack -> stack.getItem() == input.getItem()
                        && (stack.getMetadata() == OreDictionary.WILDCARD_VALUE
                        || stack.getMetadata() == input.getMetadata()));
    }

    public static void removeRecipeByName(String name) {
        removeRecipeByName(new ResourceLocation(name));
    }

    public static void removeRecipeByName(ResourceLocation rl) {
        ForgeRegistries.RECIPES.register(new DummyRecipe().setRegistryName(rl));
    }

    @SuppressWarnings("all")
    public static void addStairsRecipe(String name, ItemStack stairs, ItemStack block) {
        if (stairs == null || stairs.isEmpty() || block == null || block.isEmpty()) return;
        stairs = stairs.copy();
        stairs.setCount(4);
        block = block.copy();
        block.setCount(1);
        addShapedRecipe(name, stairs, true, new Object[]{"X  ", "XX ", "XXX", 'X', block});
    }

    public static void addSlabRecipe(String name, ItemStack slab, ItemStack block) {
        if (slab == null || slab.isEmpty() || block == null || block.isEmpty()) return;
        slab = slab.copy();
        slab.setCount(6);
        block = block.copy();
        block.setCount(1);
        addShapedRecipe(name, slab, "XXX", 'X', block);
    }

    public static void addWallRecipe(String name, ItemStack wall, ItemStack block) {
        if (wall == null || wall.isEmpty() || block == null || block.isEmpty()) return;
        wall = wall.copy();
        wall.setCount(6);
        block = block.copy();
        block.setCount(1);
        addShapedRecipe(name, wall, "XXX", "XXX", 'X', block);
    }
}
