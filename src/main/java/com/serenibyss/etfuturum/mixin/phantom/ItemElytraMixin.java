package com.serenibyss.etfuturum.mixin.phantom;

import com.serenibyss.etfuturum.items.EFMItems;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemElytra.class)
public class ItemElytraMixin {

    /**
     * @author Serenibyss
     * @reason Make Elytra repair with Phantom Membrane
     */
    @Overwrite
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == EFMItems.PHANTOM_MEMBRANE.getItem();
    }
}
