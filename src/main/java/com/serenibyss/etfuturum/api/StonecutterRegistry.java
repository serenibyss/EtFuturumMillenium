package com.serenibyss.etfuturum.api;

import com.serenibyss.etfuturum.util.ItemStackHashStrategy;
import com.serenibyss.etfuturum.util.ItemStackUtils;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class StonecutterRegistry {

    private static final StonecutterRegistry INSTANCE = new StonecutterRegistry();

    private final Object2ObjectMap<ItemStack, ObjectList<ItemStack>> recipes = new Object2ObjectOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());

    public static StonecutterRegistry instance() {
        return INSTANCE;
    }

    public void addRecipe(ItemStack input, ItemStack... output) {
        ObjectList<ItemStack> list = recipes.computeIfAbsent(input, $ -> new ObjectArrayList<>());
        Arrays.stream(output).map(ItemStack::copy).forEach(list::add);
    }

    @NotNull
    public List<ItemStack> getResultsForInput(ItemStack input) {
        ObjectList<ItemStack> list = recipes.get(input);
        if (list == null) {
            return ObjectLists.emptyList();
        }
        return ObjectLists.unmodifiable(recipes.get(input));
    }

    @ApiStatus.Internal
    public int getIndexForOutput(ItemStack input, ItemStack targetOutput) {
        List<ItemStack> outputs = recipes.get(input);
        if (outputs == null) {
            return -1;
        }
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i);
            if (ItemStackUtils.areItemStacksEqualIgnoreCount(output, targetOutput)) {
                return i;
            }
        }
        return -1;
    }

    @NotNull
    public Map<ItemStack, ? extends List<ItemStack>> getAllRecipes() {
        return Object2ObjectMaps.unmodifiable(recipes);
    }
}
