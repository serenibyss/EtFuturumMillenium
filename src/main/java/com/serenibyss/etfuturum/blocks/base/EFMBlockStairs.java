package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

@SuppressWarnings("deprecation")
public class EFMBlockStairs extends BlockStairs implements IModelRegister {

    public EFMBlockStairs(Block baseBlock, String translationKey) {
        this(baseBlock, 0, translationKey);
    }

    // the super constructor is protected...
    public EFMBlockStairs(Block baseBlock, int meta, String translationKey) {
        super(baseBlock.getStateFromMeta(meta));
        setTranslationKey(translationKey);
    }
}
