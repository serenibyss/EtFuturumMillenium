package com.serenibyss.etfuturum.recipes;

import com.serenibyss.etfuturum.api.StonecutterRegistry;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

final class StonecutterRecipes {

    static void init() {
        final StonecutterRegistry r = StonecutterRegistry.instance();

        // Stone
        r.addRecipe(new ItemStack(Blocks.STONE),
                new ItemStack(Blocks.STONEBRICK, 1, 3),    // Chiseled Stone Bricks, 1
                new ItemStack(Blocks.STONE_SLAB, 2, 5),    // Stone Bricks Slab, 2
                new ItemStack(Blocks.STONE_BRICK_STAIRS),  // Stone Brick Stairs, 1
                EFMBlocks.STONE_BRICK_WALL.getItemStack(), // Stone Brick Wall, 1
                new ItemStack(Blocks.STONEBRICK),          // Stone Bricks, 1
                EFMBlocks.STONE_SLAB.getItemStack(2),      // Stone Slab, 2
                EFMBlocks.STONE_STAIRS.getItemStack()      // Stone Stairs, 1
        );

        // Cobblestone
        r.addRecipe(new ItemStack(Blocks.COBBLESTONE),
                new ItemStack(Blocks.STONE_SLAB, 2, 3), // Cobblestone Slab, 2
                new ItemStack(Blocks.STONE_STAIRS),     // Cobblestone Stairs, 1
                new ItemStack(Blocks.COBBLESTONE_WALL)  // Cobblestone Wall, 1
        );

        // Mossy Cobblestone
        r.addRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE),
                // EFMBlocks.SLAB.getItemStack(2, meta),     // Mossy Cobblestone Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta),    // Mossy Cobblestone Stairs, 1
                new ItemStack(Blocks.COBBLESTONE_WALL, 1, 1) // Mossy Cobblestone Wall, 1
        );

        // Smooth Stone
        r.addRecipe(EFMBlocks.SMOOTH_STONE.getItemStack(),
                new ItemStack(Blocks.STONE_SLAB, 2) // Smooth Stone Slab, 2
        );

        // Stone Bricks
        r.addRecipe(new ItemStack(Blocks.STONEBRICK),
                new ItemStack(Blocks.STONEBRICK, 1, 3),   // Chiseled Stone Bricks, 1
                new ItemStack(Blocks.STONE_SLAB, 2, 5),   // Stone Bricks Slab, 2
                new ItemStack(Blocks.STONE_BRICK_STAIRS), // Stone Brick Stairs, 1
                EFMBlocks.STONE_BRICK_WALL.getItemStack() // Stone Brick Wall, 1
        );

        // Mossy Stone Bricks
        // r.addRecipe(new ItemStack(Blocks.STONEBRICK, 1, 1),
        //         EFMBlocks.SLAB.getItemStack(2, meta),   // Mossy Stone Brick Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta), // Mossy Stone Brick Stairs, 1
        //         EFMBlocks.WALL.getItemStack(1, meta)    // Mossy Stone Brick Wall, 1
        // );

        // Granite
        r.addRecipe(new ItemStack(Blocks.STONE, 1, 1),
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Granite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta), // Granite Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta),  // Granite Wall, 1
                new ItemStack(Blocks.STONE, 1, 2)         // Polished Granite, 1
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Granite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta)  // Polished Granite Stairs, 1
        );

        // Polished Granite
        // r.addRecipe(new ItemStack(Blocks.STONE, 1, 2),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Granite Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Polished Granite Stairs, 1
        // );

        // Diorite
        r.addRecipe(new ItemStack(Blocks.STONE, 1, 3),
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Diorite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta), // Diorite Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta),  // Diorite Wall, 1
                new ItemStack(Blocks.STONE, 1, 4)         // Polished Diorite, 1
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Diorite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta)  // Polished Diorite Stairs, 1
        );

        // Polished Diorite
        // r.addRecipe(new ItemStack(Blocks.STONE, 1, 4),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Diorite Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Polished Diorite Stairs, 1
        // );

        // Andesite
        r.addRecipe(new ItemStack(Blocks.STONE, 1, 5),
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Andesite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta), // Andesite Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta),  // Andesite Wall, 1
                new ItemStack(Blocks.STONE, 1, 6)         // Polished Andesite, 1
                // EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Andesite Slab, 2
                // EFMBlocks.STAIR.getItemStack(1, meta)  // Polished Andesite Stairs, 1
        );

        // Polished Andesite
        // r.addRecipe(new ItemStack(Blocks.STONE, 1, 6),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Polished Andesite Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Polished Andesite Stairs, 1
        // );

        // Cobbled Deepslate
        // r.addRecipe(EFMBlocks.DEEPSLATE.getItemStack(1, meta),
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Chiseled Deepslate, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Cobbled Deepslate Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Cobbled Deepslate Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Cobbled Deepslate Wall, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Brick Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Brick Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Brick Wall, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Deepslate Bricks, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Tile Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Tile Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Tile Wall, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Deepslate Tiles, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Polished Deepslate, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Polished Deepslate Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Polished Deepslate Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta)    // Polished Deepslate Wall, 1
        // );

        // Polished Deepslate
        // r.addRecipe(EFMBlocks.DEEPSLATE.getItemStack(1, meta),
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Brick Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Brick Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Brick Wall, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Deepslate Bricks, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Tile Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Tile Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Tile Wall, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Deepslate Tiles, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Polished Deepslate Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Polished Deepslate Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta)    // Polished Deepslate Wall, 1
        // );

        // Deepslate Bricks
        // r.addRecipe(EFMBlocks.DEEPSLATE.getItemStack(1, meta),
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Brick Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Brick Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Brick Wall, 1
        //         EFMBlocks.DEEPSLATE_SLAB.getItemStack(2, meta),   // Deepslate Tile Slab, 2
        //         EFMBlocks.DEEPSLATE_STAIRS.getItemStack(1, meta), // Deepslate Tile Stairs, 1
        //         EFMBlocks.DEEPSLATE_WALL.getItemStack(1, meta),   // Deepslate Tile Wall, 1
        //         EFMBlocks.DEEPSLATE.getItemStack(1, meta),        // Deepslate Tiles, 1
        // );

        // Bricks
        r.addRecipe(new ItemStack(Blocks.BRICK_BLOCK),
                new ItemStack(Blocks.STONE_SLAB, 2, 4), // Brick Slab, 2
                new ItemStack(Blocks.BRICK_STAIRS)      // Brick Stairs, 1
        //         EFMBlocks.WALL.getItemStack(1, meta) // Brick Wall, 1
        );

        // Mud Bricks
        // r.addRecipe(EFMBlocks.MUD_BRICKS.getItemStack(),
        //         EFMBlocks.MUD_BRICK_SLAB.getItemStack(2),  // Mud Brick Slab, 2
        //         EFMBlocks.MUD_BRICK_STAIRS.getItemStack(), // Mud Brick Stairs, 1
        //         EFMBlocks.MUD_BRICK_WALL.getItemStack()    // Mud Brick Wall, 1
        // );

        // Sandstone
        r.addRecipe(new ItemStack(Blocks.SANDSTONE),
                new ItemStack(Blocks.SANDSTONE, 1, 1),   // Chiseled Sandstone, 1
                new ItemStack(Blocks.SANDSTONE, 1, 2),   // Cut Sandstone, 1
                // EFMBlocks.SLAB.getItemStack(2, meta), // Cut Sandstone Slab, 2
                new ItemStack(Blocks.STONE_SLAB, 2, 1),  // Sandstone Slab, 2
                new ItemStack(Blocks.SANDSTONE_STAIRS)   // Sandstone Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta)  // Sandstone Wall, 1
        );

        // Smooth Sandstone
        // r.addRecipe(EFMBlocks.SMOOTH_SANDSTONE.getItemStack(),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Smooth Sandstone Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Smooth Sandstone Stairs, 1
        // );

        // Cut Sandstone
        // r.addRecipe(new ItemStack(Blocks.SANDSTONE, 1, 2),
        //         EFMBlocks.SLAB.getItemStack(2, meta) // Cut Sandstone Slab, 2
        // );

        // Red Sandstone
        r.addRecipe(new ItemStack(Blocks.RED_SANDSTONE),
                new ItemStack(Blocks.RED_SANDSTONE, 1, 1), // Chiseled Red Sandstone, 1
                new ItemStack(Blocks.RED_SANDSTONE, 1, 2), // Cut Red Sandstone, 1
                // EFMBlocks.SLAB.getItemStack(2, meta),   // Cut Red Sandstone Slab, 2
                new ItemStack(Blocks.STONE_SLAB2, 2),      // Red Sandstone Slab, 2
                new ItemStack(Blocks.RED_SANDSTONE_STAIRS) // Red Sandstone Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta)    // Red Sandstone Wall, 1
        );

        // Smooth Red Sandstone
        // r.addRecipe(EFMBlocks.SMOOTH_SANDSTONE.getItemStack(1, meta),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Smooth Red Sandstone Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Smooth Red Sandstone Stairs, 1
        // );

        // Cut Red Sandstone
        // r.addRecipe(new ItemStack(Blocks.RED_SANDSTONE),
        //         EFMBlocks.SLAB.getItemStack(2, meta) // Cut Red Sandstone Slab, 2
        // );

        // Prismarine Bricks
        // r.addRecipe(new ItemStack(Blocks.PRISMARINE, 1, 1),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Prismarine Brick Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Prismarine Brick Stairs, 1
        // );

        // Dark Prismarine
        // r.addRecipe(new ItemStack(Blocks.PRISMARINE, 1, 2),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Dark Prismarine Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Dark Prismarine Stairs, 1
        // );

        // Nether Bricks
        r.addRecipe(new ItemStack(Blocks.NETHER_BRICK),
                // EFMBlocks.CHISELED_NETHER_BRICK.getItemStack(), // Chiseled Nether Brick, 1
                new ItemStack(Blocks.STONE_SLAB, 2, 6),            // Nether Brick Slab, 2
                new ItemStack(Blocks.NETHER_BRICK_STAIRS)          // Nether Brick Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta)            // Nether Brick Wall, 1
        );

        // Red Nether Bricks
        // r.addRecipe(new ItemStack(Blocks.RED_NETHER_BRICK),
        //         EFMBlocks.SLAB.getItemStack(2, meta),   // Red Nether Brick Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta), // Red Nether Brick Stairs, 1
        //         EFMBlocks.WALL.getItemStack(1, meta)    // Red Nether Brick Wall, 1
        // );

        // Basalt
        // r.addRecipe(EFMBlocks.BASALT.getItemStack(1, meta),
        //         EFMBlocks.BASALT.getItemStack(1, meta) // Polished Basalt, 1
        // );

        // Blackstone
        // r.addRecipe(EFMBlocks.BLACKSTONE.getItemStack(),
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Blackstone Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Blackstone Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta),   // Blackstone Wall, 1
        //         EFMBlocks.BLACKSTONE.getItemStack(1, meta),        // Chiseled Polished Blackstone, 1
        //         EFMBlocks.BLACKSTONE.getItemStack(1, meta),        // Polished Blackstone, 1
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Polished Blackstone Brick Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Polished Blackstone Brick Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta),   // Polished Blackstone Brick Wall, 1
        //         EFMBlocks.BLACKSTONE.getItemStack(1, meta),        // Polished Blackstone Bricks, 1
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Polished Blackstone Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Polished Blackstone Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta)    // Polished Blackstone Wall, 1
        // );

        // Polished Blackstone
        // r.addRecipe(EFMBlocks.BLACKSTONE.getItemStack(1, meta),
        //         EFMBlocks.BLACKSTONE.getItemStack(1, meta),        // Chiseled Polished Blackstone, 1
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Polished Blackstone Brick Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Polished Blackstone Brick Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta),   // Polished Blackstone Brick Wall, 1
        //         EFMBlocks.BLACKSTONE.getItemStack(1, meta),        // Polished Blackstone Bricks, 1
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Polished Blackstone Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Polished Blackstone Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta)    // Polished Blackstone Wall, 1
        // );

        // Polished Blackstone Bricks
        // r.addRecipe(EFMBlocks.BLACKSTONE.getItemStack(1, meta),
        //         EFMBlocks.BLACKSTONE_SLAB.getItemStack(2, meta),   // Polished Blackstone Slab, 2
        //         EFMBlocks.BLACKSTONE_STAIRS.getItemStack(1, meta), // Polished Blackstone Stairs, 1
        //         EFMBlocks.BLACKSTONE_WALL.getItemStack(1, meta)    // Polished Blackstone Wall, 1
        // );

        // End Stone
        r.addRecipe(new ItemStack(Blocks.END_STONE),
                // EFMBlocks.SLAB.getItemStack(2, meta),   // End Stone Brick Slab, 2
                // EFMBlocks.STAIRS.getItemStack(1, meta), // End Stone Brick Stairs, 1
                // EFMBlocks.WALL.getItemStack(1, meta),   // End Stone Brick Wall, 1
                new ItemStack(Blocks.END_BRICKS)           // End Stone Bricks, 1
        );

        // End Stone Bricks
        // r.addRecipe(new ItemStack(Blocks.END_BRICKS),
        //         EFMBlocks.SLAB.getItemStack(2, meta),   // End Stone Brick Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta), // End Stone Brick Stairs, 1
        //         EFMBlocks.WALL.getItemStack(1, meta)    // End Stone Brick Wall, 1
        // );

        // Purpur Block
        r.addRecipe(new ItemStack(Blocks.PURPUR_BLOCK),
                new ItemStack(Blocks.PURPUR_PILLAR),  // Purpur Pillar, 1
                new ItemStack(Blocks.PURPUR_SLAB, 2), // Purpur Slab, 2
                new ItemStack(Blocks.PURPUR_STAIRS)   // Purpur Stairs, 1
        );

        // Block of Quartz
        r.addRecipe(new ItemStack(Blocks.QUARTZ_BLOCK),
                new ItemStack(Blocks.QUARTZ_BLOCK, 1, 1),  // Chiseled Quartz Block, 1
                // EFMBlocks.QUARTZ_BRICKS.getItemStack(), // Quartz Bricks, 1
                new ItemStack(Blocks.QUARTZ_BLOCK, 1, 2),  // Quartz Pillar, 1
                new ItemStack(Blocks.STONE_SLAB, 2, 7),    // Quartz Slab, 2
                new ItemStack(Blocks.QUARTZ_STAIRS)        // Quartz Stairs, 1
        );

        // Smooth Quartz
        // r.addRecipe(EFMBlocks.SMOOTH_QUARTZ.getItemStack(),
        //         EFMBlocks.SLAB.getItemStack(2, meta),  // Smooth Quartz Slab, 2
        //         EFMBlocks.STAIRS.getItemStack(1, meta) // Smooth Quartz Stairs, 1
        // );

        // Copper stuff
        // todo
    }
}
