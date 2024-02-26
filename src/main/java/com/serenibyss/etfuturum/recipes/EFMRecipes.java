package com.serenibyss.etfuturum.recipes;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.api.StrippingRegistry;
import com.serenibyss.etfuturum.blocks.BlockDeepslate;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.blocks.base.EFMBlock;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.RecipeSorter;

import static com.serenibyss.etfuturum.recipes.RecipeHelpers.*;

public class EFMRecipes {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onRegisterRecipe(RegistryEvent.Register<IRecipe> event) {
        RecipeSorter.register(EFMTags.MODID + ":shaped", EFMShapedRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shaped");
        RecipeSorter.register(EFMTags.MODID + ":shapeless", EFMShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "before:minecraft:shapeless");

        additions();
        if (Features.MC14.stonecutter.isEnabled()) {
            StonecutterRecipes.init();
        }
        if (Features.MC13.stripping.isEnabled()) {
            strippingRecipes();
        }
    }

    private static void additions() {
        addShapedRecipe("barrel", EFMBlocks.BARREL.getItemStack(), "XSX", "X X", "XSX", 'X', "plankWood", 'S', "slabWood");
        addShapedRecipe("stonecutter", EFMBlocks.STONECUTTER.getItemStack(), " X ", "SSS", 'X', "ingotIron", 'S', new ItemStack(Blocks.STONE));

        if(Features.MISC.newStairs.isEnabled()) {
            addStairsRecipe("stone_stairs", EFMBlocks.STONE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE));
            addStairsRecipe("granite_stairs", EFMBlocks.GRANITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 1));
            addStairsRecipe("polished_granite_stairs", EFMBlocks.POLISHED_GRANITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 2));
            addStairsRecipe("diorite_stairs", EFMBlocks.DIORITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 3));
            addStairsRecipe("polished_diorite_stairs", EFMBlocks.POLISHED_DIORITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 4));
            addStairsRecipe("andesite_stairs", EFMBlocks.ANDESITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 5));
            addStairsRecipe("polished_andesite_stairs", EFMBlocks.POLISHED_ANDESITE_STAIRS.getItemStack(), new ItemStack(Blocks.STONE, 1, 6));
            addStairsRecipe("mossy_cobblestone_stairs", EFMBlocks.MOSSY_COBBLESTONE_STAIRS.getItemStack(), new ItemStack(Blocks.MOSSY_COBBLESTONE));
            addStairsRecipe("mossy_stone_brick_stairs", EFMBlocks.MOSSY_STONE_BRICK_STAIRS.getItemStack(), new ItemStack(Blocks.STONEBRICK, 1, 1));
        }

        if(Features.MC18.deepslate.isEnabled()) {
            addStairsRecipe("cobbled_deepslate_stairs", EFMBlocks.COBBLED_DEEPSLATE_STAIRS.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 0));
            addStairsRecipe("polished_deepslate_stairs", EFMBlocks.POLISHED_DEEPSLATE_STAIRS.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 2));
            addStairsRecipe("deepslate_brick_stairs", EFMBlocks.DEEPSLATE_BRICK_STAIRS.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 3));
            addStairsRecipe("deepslate_tile_stairs", EFMBlocks.DEEPSLATE_TILE_STAIRS.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 5));
        }

        if(Features.MISC.newSlabs.isEnabled()) {
            addSlabRecipe("stone_slab", EFMBlocks.STONE_SLAB.getItemStack(), new ItemStack(Blocks.STONE));
            if (Features.MC14.smoothStone.isEnabled()) {
                addSlabRecipe("minecraft:stone_slab", new ItemStack(Blocks.STONE_SLAB), EFMBlocks.SMOOTH_STONE.getItemStack());
            }
            addSlabRecipe("mossy_cobblestone_slab", EFMBlocks.MOSSY_COBBLESTONE_SLAB.getItemStack(), new ItemStack(Blocks.MOSSY_COBBLESTONE));
            addSlabRecipe("mossy_stone_brick_slab", EFMBlocks.MOSSY_STONE_BRICK_SLAB.getItemStack(), new ItemStack(Blocks.STONEBRICK, 1, 1));
            addSlabRecipe("granite_slab", EFMBlocks.GRANITE_SLAB.getItemStack(), new ItemStack(Blocks.STONE, 1, 1));
            addSlabRecipe("diorite_slab", EFMBlocks.DIORITE_SLAB.getItemStack(), new ItemStack(Blocks.STONE, 1, 3));
            addSlabRecipe("andesite_slab", EFMBlocks.ANDESITE_SLAB.getItemStack(), new ItemStack(Blocks.STONE, 1, 5));
        }

        if(Features.MC18.deepslate.isEnabled()) {
            addSlabRecipe("cobbled_deepslate_slab", EFMBlocks.COBBLED_DEEPSLATE_SLAB.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 0));
            addSlabRecipe("polished_deepslate_slab", EFMBlocks.POLISHED_DEEPSLATE_SLAB.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 2));
            addSlabRecipe("deepslate_brick_slab", EFMBlocks.DEEPSLATE_BRICK_SLAB.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 3));
            addSlabRecipe("deepslate_tile_slab", EFMBlocks.DEEPSLATE_TILE_SLAB.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 5));
        }

        if(Features.MISC.newWalls.isEnabled()) {
            addWallRecipe("stone_brick_wall", EFMBlocks.STONE_BRICK_WALL.getItemStack(), new ItemStack(Blocks.STONEBRICK));
            addWallRecipe("mossy_stone_brick_wall", EFMBlocks.MOSSY_STONE_BRICK_WALL.getItemStack(), new ItemStack(Blocks.STONEBRICK, 1, 1));
            addWallRecipe("granite_wall", EFMBlocks.GRANITE_WALL.getItemStack(), new ItemStack(Blocks.STONE, 1, 1));
            addWallRecipe("diorite_wall", EFMBlocks.DIORITE_WALL.getItemStack(), new ItemStack(Blocks.STONE, 1, 3));
            addWallRecipe("andesite_wall", EFMBlocks.ANDESITE_WALL.getItemStack(), new ItemStack(Blocks.STONE, 1, 5));
        }

        if(Features.MC18.deepslate.isEnabled()) {
            addWallRecipe("cobbled_deepslate_wall", EFMBlocks.COBBLED_DEEPSLATE_WALL.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 0));
            addWallRecipe("polished_deepslate_wall", EFMBlocks.POLISHED_DEEPSLATE_WALL.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 2));
            addWallRecipe("deepslate_brick_wall", EFMBlocks.DEEPSLATE_BRICK_WALL.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 3));
            addWallRecipe("deepslate_tile_wall", EFMBlocks.DEEPSLATE_TILE_WALL.getItemStack(), new ItemStack(EFMBlocks.DEEPSLATE.getBlock(), 1, 5));
        }

        // todo: Uncomment when Nautilus Shell and Heart of the Sea are added
        //addShapedRecipe("conduit", EFMBlocks.CONDUIT.getItemStack(), "XXX", "XSX", "XXX", 'X', EFMItems.NAUTILUS_SHELL.getItemStack(), 'S', EFMItems.HEART_OF_THE_SEA.getItemStack());

        // replacing planks recipe with our own that uses oredict
        // don't need to remove because we're reusing the MC IDs for compatibility
        if (Features.MC13.stripping.isEnabled()) {
            addShapelessRecipe("minecraft:oak_planks", new ItemStack(Blocks.PLANKS, 4, 0), EFMOreDict.LOG_WOOD_OAK);
            addShapelessRecipe("minecraft:spruce_planks", new ItemStack(Blocks.PLANKS, 4, 1), EFMOreDict.LOG_WOOD_SPRUCE);
            addShapelessRecipe("minecraft:birch_planks", new ItemStack(Blocks.PLANKS, 4, 2), EFMOreDict.LOG_WOOD_BIRCH);
            addShapelessRecipe("minecraft:jungle_planks", new ItemStack(Blocks.PLANKS, 4, 3), EFMOreDict.LOG_WOOD_JUNGLE);
            addShapelessRecipe("minecraft:acacia_planks", new ItemStack(Blocks.PLANKS, 4, 4), EFMOreDict.LOG_WOOD_ACACIA);
            addShapelessRecipe("minecraft:dark_oak_planks", new ItemStack(Blocks.PLANKS, 4, 5), EFMOreDict.LOG_WOOD_DARK_OAK);
        }

        addShapedRecipe("turtle_helmet", EFMItems.TURTLE_HELMET.getItemStack(), "XXX", "X X", 'X', EFMItems.SCUTE.getItemStack());

        if (Features.MC18.deepslate.isEnabled()) {
            RecipeHelpers.addSmeltingRecipe(EFMBlocks.DEEPSLATE.getItemStack(1, 3), EFMBlocks.DEEPSLATE.getItemStack(1, 4), 0.1f);
            RecipeHelpers.addSmeltingRecipe(EFMBlocks.DEEPSLATE.getItemStack(1, 5), EFMBlocks.DEEPSLATE.getItemStack(1, 6), 0.1f);
        }
    }

    @SuppressWarnings("deprecation")
    private static void strippingRecipes() {
        StrippingRegistry r = StrippingRegistry.instance();
        Block stripped_log_1 = EFMBlocks.STRIPPED_LOG_1.getBlock();
        Block stripped_log_2 = EFMBlocks.STRIPPED_LOG_2.getBlock();

        if (stripped_log_1 == null || stripped_log_2 == null) {
            return;
        }

        // states from logs directly map to stripped_log
        for (int i = 0; i <= 11; i++) {
            r.registerConversion(Blocks.LOG.getStateFromMeta(i), stripped_log_1.getStateFromMeta(i));
        }

        r.registerConversion(Blocks.LOG2.getStateFromMeta(0), stripped_log_2.getStateFromMeta(0));
        r.registerConversion(Blocks.LOG2.getStateFromMeta(1), stripped_log_2.getStateFromMeta(1));
        r.registerConversion(Blocks.LOG2.getStateFromMeta(4), stripped_log_2.getStateFromMeta(4));
        r.registerConversion(Blocks.LOG2.getStateFromMeta(5), stripped_log_2.getStateFromMeta(5));
        r.registerConversion(Blocks.LOG2.getStateFromMeta(8), stripped_log_2.getStateFromMeta(8));
        r.registerConversion(Blocks.LOG2.getStateFromMeta(9), stripped_log_2.getStateFromMeta(9));
    }
}
