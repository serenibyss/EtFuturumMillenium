package com.serenibyss.etfuturum.compat.jei.category;

import com.serenibyss.etfuturum.containers.ContainerStonecutter;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StonecutterTransferInfo implements IRecipeTransferInfo<ContainerStonecutter> {

    @NotNull
    @Override
    public Class<ContainerStonecutter> getContainerClass() {
        return ContainerStonecutter.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return StonecutterCategory.UID;
    }

    @Override
    public boolean canHandle(@NotNull ContainerStonecutter container) {
        return true;
    }

    @NotNull
    @Override
    public List<Slot> getRecipeSlots(@NotNull ContainerStonecutter container) {
        return Collections.singletonList(container.getSlot(0));
    }

    @NotNull
    @Override
    public List<Slot> getInventorySlots(@NotNull ContainerStonecutter container) {
        return container.inventorySlots.subList(2, container.inventorySlots.size());
    }
}
