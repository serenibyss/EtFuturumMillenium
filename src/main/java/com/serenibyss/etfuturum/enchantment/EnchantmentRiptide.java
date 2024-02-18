package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.load.enums.EFMEnumEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentRiptide extends Enchantment {

    protected EnchantmentRiptide(Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, EFMEnumEnchantmentType.TRIDENT, slots);
        this.setName("riptide");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 7;
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
        return super.canApplyTogether(ench) && ench != EFMEnchantments.LOYALTY && ench != EFMEnchantments.CHANNELING;
    }
}
