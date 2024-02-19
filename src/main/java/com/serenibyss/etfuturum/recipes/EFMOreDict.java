package com.serenibyss.etfuturum.recipes;

import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class EFMOreDict {
    public static String LOG_WOOD = "logWood";
    public static String LOG_WOOD_OAK = "logWoodOak";
    public static String LOG_WOOD_SPRUCE = "logWoodSpruce";
    public static String LOG_WOOD_BIRCH = "logWoodBirch";
    public static String LOG_WOOD_JUNGLE = "logWoodJungle";
    public static String LOG_WOOD_ACACIA = "logWoodAcacia";
    public static String LOG_WOOD_DARK_OAK = "logWoodDarkOak";

    public static void init() {
        if (Features.MC13.stripping.isEnabled()) {
            // Add oredicts for the different types
            RecipeHelpers.registerOre(LOG_WOOD_OAK, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 0));
            RecipeHelpers.registerOre(LOG_WOOD_SPRUCE, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 1));
            RecipeHelpers.registerOre(LOG_WOOD_BIRCH, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 2));
            RecipeHelpers.registerOre(LOG_WOOD_JUNGLE, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 3));
            RecipeHelpers.registerOre(LOG_WOOD_ACACIA, EFMBlocks.STRIPPED_LOG_2.getItemStack(1, 0));
            RecipeHelpers.registerOre(LOG_WOOD_DARK_OAK, EFMBlocks.STRIPPED_LOG_2.getItemStack(1, 1));
            // Same for the vanilla logs
            RecipeHelpers.registerOre(LOG_WOOD_OAK, new ItemStack(Blocks.LOG, 1, 0));
            RecipeHelpers.registerOre(LOG_WOOD_SPRUCE, new ItemStack(Blocks.LOG, 1, 1));
            RecipeHelpers.registerOre(LOG_WOOD_BIRCH, new ItemStack(Blocks.LOG, 1, 2));
            RecipeHelpers.registerOre(LOG_WOOD_JUNGLE, new ItemStack(Blocks.LOG, 1, 3));
            RecipeHelpers.registerOre(LOG_WOOD_ACACIA, new ItemStack(Blocks.LOG2, 1, 0));
            RecipeHelpers.registerOre(LOG_WOOD_DARK_OAK, new ItemStack(Blocks.LOG2, 1, 1));

            // Add general logWood to stripped logs
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 0));
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 1));
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 2));
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_1.getItemStack(1, 3));
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_2.getItemStack(1, 0));
            RecipeHelpers.registerOre(LOG_WOOD, EFMBlocks.STRIPPED_LOG_2.getItemStack(1, 1));
        }
    }
}
