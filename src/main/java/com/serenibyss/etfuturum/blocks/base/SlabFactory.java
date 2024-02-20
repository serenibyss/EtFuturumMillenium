package com.serenibyss.etfuturum.blocks.base;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/** A place to construct Single+Double Slabs together without needing lots of tiny classes. */
public class SlabFactory {

    public static final SlabFactory STONE_SLAB = new SlabFactory(
            new EFMBlock.Settings(Material.ROCK, MapColor.STONE)
                    .hardness(2.0f).resistance(6.0f)
                    .creativeTab(CreativeTabs.BUILDING_BLOCKS)
                    .translationKey("stone_slab"));

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

    public EFMBlockHalfSlab getHalfSlab() {
        return halfSlab;
    }

    public EFMBlockDoubleSlab getDoubleSlab() {
        return doubleSlab;
    }
}
