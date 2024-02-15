package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.load.enums.EFMEnumEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentChanneling extends Enchantment {

    protected EnchantmentChanneling(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, EFMEnumEnchantmentType.TRIDENT, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) ;
    }
}
