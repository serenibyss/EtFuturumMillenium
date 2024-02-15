package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.load.enums.EFMEnumEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentLoyalty extends Enchantment {

    protected EnchantmentLoyalty(Rarity rarityIn,  EntityEquipmentSlot... slots) {
        super(rarityIn, EFMEnumEnchantmentType.TRIDENT, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + enchantmentLevel * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench);
    }
}
