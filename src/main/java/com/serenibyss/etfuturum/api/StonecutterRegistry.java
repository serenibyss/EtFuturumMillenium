package com.serenibyss.etfuturum.api;

import com.serenibyss.etfuturum.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class StonecutterRegistry {

    private static final StonecutterRegistry INSTANCE = new StonecutterRegistry();

    private final Map<ItemStack, ObjectList<ItemStack>> recipes = new Object2ObjectOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());

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

    //public boolean isValidRecipe(ItemStack input, ItemStack output) {
    //    ObjectList<ItemStack> list = recipes.get(input);
    //    if (list == null) {
    //        return false;
    //    }
    //    return true;
    //}
}
