package com.serenibyss.etfuturum.blocks.base;

public abstract class EFMBlockDoubleSlab extends EFMBlockSlab {

    public EFMBlockDoubleSlab(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public EFMBlockDoubleSlab getDoubleSlab() {
        return this;
    }

    @Override
    public void registerModel() {}
}
