package com.serenibyss.etfuturum.items.base;

import com.serenibyss.etfuturum.blocks.base.IMultiItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class EFMItemBlock extends ItemBlock {
    public EFMItemBlock(Block block, int maxDamage) {
        super(block);
        this.setMaxDamage(maxDamage);
        this.setHasSubtypes(true);
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
