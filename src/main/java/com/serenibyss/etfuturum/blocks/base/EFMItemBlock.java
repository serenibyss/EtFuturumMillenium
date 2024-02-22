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
        if (block instanceof IMultiItemBlock multiItemBlock) {
            this.setMaxDamage(multiItemBlock.getMaxItemDamage());
            this.setHasSubtypes(multiItemBlock.getHasItemSubtypes());
        } else {
            this.setMaxDamage(0);
        }
    }

    public EFMItemBlock(EFMBlock block) {
        super(block);
        EnumRarity rarity = block.settings.rarity;
        if (rarity == null) rarity = EnumRarity.COMMON;
        this.rarity = rarity;
        this.setMaxDamage(block.getMaxItemDamage());
        this.setHasSubtypes(block.getHasItemSubtypes());
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return rarity;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (block instanceof IMultiItemBlock multiItemBlock) {
            return multiItemBlock.getTranslationKey(stack.getMetadata());
        } else {
            return super.getTranslationKey(stack);
        }
    }
}
