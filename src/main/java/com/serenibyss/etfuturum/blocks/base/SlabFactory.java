package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.blocks.BlockDeepslate;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import net.minecraft.init.Blocks;

/** A place to construct Single+Double Slabs together without needing lots of tiny classes. */
public class SlabFactory {

    public static final SlabFactory STONE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE).translationKey("stone_slab"));
    public static final SlabFactory MOSSY_COBBLESTONE = new SlabFactory(EFMBlock.Settings.from(Blocks.MOSSY_COBBLESTONE).translationKey("mossy_cobblestone_slab"));
    public static final SlabFactory MOSSY_STONE_BRICK = new SlabFactory(EFMBlock.Settings.from(Blocks.STONEBRICK, 1).translationKey("mossy_stone_brick_slab"));
    public static final SlabFactory GRANITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 1).translationKey("granite_slab"));
    public static final SlabFactory DIORITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 3).translationKey("diorite_slab"));
    public static final SlabFactory ANDESITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 5).translationKey("andesite_slab"));
    public static final SlabFactory COBBLED_DEEPSLATE = new SlabFactory(EFMBlock.Settings.from(EFMBlocks.DEEPSLATE.getBlock(), 0).translationKey("cobbled_deepslate_slab"));
    public static final SlabFactory POLISHED_DEEPSLATE = new SlabFactory(EFMBlock.Settings.from(EFMBlocks.DEEPSLATE.getBlock(), 2).translationKey("polished_deepslate_slab"));
    public static final SlabFactory DEEPSLATE_BRICK = new SlabFactory(EFMBlock.Settings.from(EFMBlocks.DEEPSLATE.getBlock(), 3).translationKey("deepslate_brick_slab"));
    public static final SlabFactory DEEPSLATE_TILE = new SlabFactory(EFMBlock.Settings.from(EFMBlocks.DEEPSLATE.getBlock(), 5).translationKey("deepslate_tile_slab"));

    private final EFMBlockHalfSlab halfSlab;
    private final EFMBlockDoubleSlab doubleSlab;

    private SlabFactory(EFMBlock.Settings settings) {
        this.halfSlab = new EFMBlockHalfSlab(settings) {
            @Override
            public EFMBlockDoubleSlab getDoubleSlab() {
                return doubleSlab;
            }
        };
        this.doubleSlab = new EFMBlockDoubleSlab(settings) {
            @Override
            public EFMBlockHalfSlab getHalfSlab() {
                return halfSlab;
            }
        };
    }

    public EFMBlockHalfSlab getHalf() {
        return halfSlab;
    }

    public EFMBlockDoubleSlab getDouble() {
        return doubleSlab;
    }
}
