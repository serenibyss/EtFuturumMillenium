package com.serenibyss.etfuturum.recipes;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.api.StonecutterRegistry;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.load.feature.Features;
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
            stonecutterRecipes();
        }
    }

    private static void additions() {
        addShapedRecipe("barrel", EFMBlocks.BARREL.getItemStack(), "XSX", "X X", "XSX", 'X', "plankWood", 'S', "slabWood");
        addShapedRecipe("stonecutter", EFMBlocks.STONECUTTER.getItemStack(), " X ", "SSS", 'X', "ingotIron", 'S', new ItemStack(Blocks.STONE));
    }

    // todo add in the remaining recipes. make sure that the order matches latest vanilla
    private static void stonecutterRecipes() {
        StonecutterRegistry r = StonecutterRegistry.instance();
        r.addRecipe(new ItemStack(Blocks.COBBLESTONE),
                new ItemStack(Blocks.STONE_STAIRS),
                new ItemStack(Blocks.STONE_SLAB, 2, 3),
                new ItemStack(Blocks.COBBLESTONE_WALL));
    }
}
