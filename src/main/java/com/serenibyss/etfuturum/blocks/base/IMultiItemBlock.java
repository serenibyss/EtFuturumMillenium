package com.serenibyss.etfuturum.blocks.base;

public interface IMultiItemBlock {
    boolean getHasItemSubtypes();

    default int getMaxItemDamage() {
        return 0;
    }

    String getTranslationKey(int meta);
}
