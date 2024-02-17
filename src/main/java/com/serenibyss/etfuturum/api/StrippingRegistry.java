package com.serenibyss.etfuturum.api;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StrippingRegistry {
    private static final StrippingRegistry INSTANCE = new StrippingRegistry();

    private final Object2ObjectMap<IBlockState, IBlockState> map = new Object2ObjectOpenHashMap<>();

    public static StrippingRegistry instance() {
        return INSTANCE;
    }

    public void registerConversion(IBlockState input, IBlockState output) {
        this.map.put(input, output);
    }

    @Nullable
    public IBlockState getStrippedBlockState(IBlockState input) {
        return this.map.get(input);
    }

    public Map<IBlockState, IBlockState> getAllStrippingConversions() {
        return Object2ObjectMaps.unmodifiable(map);
    }
}
