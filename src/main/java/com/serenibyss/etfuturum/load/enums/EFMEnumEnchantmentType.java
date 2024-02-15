package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EFMEnumEnchantmentType {

    public static EnumEnchantmentType TRIDENT;

    protected static void init() {
        // todo:onion
        //TRIDENT = addEnchantmentType(MC13.trident, "trident", );
    }

    private static EnumEnchantmentType addEnchantmentType(Feature feature, String name, Predicate<Item> predicate) {
        if (!feature.isEnabled()) {
            return null;
        }
        return EnumHelper.addEnchantmentType(name, new com.google.common.base.Predicate<Item>() {
            @Override
            public boolean apply(@Nullable Item input) {
                return predicate.test(input);
            }
        });
    }
}
