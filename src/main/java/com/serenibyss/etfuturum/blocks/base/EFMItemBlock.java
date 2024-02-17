package com.serenibyss.etfuturum.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public class EFMItemBlock extends ItemBlock {

    private final EnumRarity rarity;

    public EFMItemBlock(Block block) {
        super(block);
        this.rarity = EnumRarity.COMMON;
    }

    public EFMItemBlock(EFMBlock block) {
        super(block);
        EnumRarity rarity = block.settings.rarity;
        if (rarity == null) rarity = EnumRarity.COMMON;
        this.rarity = rarity;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return rarity;
    }
}
