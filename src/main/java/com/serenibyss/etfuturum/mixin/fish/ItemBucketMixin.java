package com.serenibyss.etfuturum.mixin.fish;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBucket.class)
public class ItemBucketMixin {

    @Shadow
    private Block containedBlock;

    public Block getContainedBlock() {
        return containedBlock;
    }
}
