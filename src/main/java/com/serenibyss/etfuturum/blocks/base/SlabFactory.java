package com.serenibyss.etfuturum.blocks.base;

import net.minecraft.init.Blocks;

/** A place to construct Single+Double Slabs together without needing lots of tiny classes. */
public class SlabFactory {

    public static final SlabFactory STONE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE).translationKey("stone_slab"));
    public static final SlabFactory MOSSY_COBBLESTONE = new SlabFactory(EFMBlock.Settings.from(Blocks.MOSSY_COBBLESTONE).translationKey("mossy_cobblestone_slab"));
    public static final SlabFactory MOSSY_STONE_BRICK = new SlabFactory(EFMBlock.Settings.from(Blocks.STONEBRICK, 1).translationKey("mossy_stone_brick_slab"));
    public static final SlabFactory GRANITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 1).translationKey("granite_slab"));
    public static final SlabFactory DIORITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 3).translationKey("diorite_slab"));
    public static final SlabFactory ANDESITE = new SlabFactory(EFMBlock.Settings.from(Blocks.STONE, 5).translationKey("andesite_slab"));

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
