package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.load.enums.EFMEnumEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentImpaling extends Enchantment{
    protected EnchantmentImpaling(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, EFMEnumEnchantmentType.TRIDENT, slots);
        this.setName("impaling");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return creatureType == EnumCreatureAttribute.valueOf("water") ? (float)level * 2.5F : 0.0F;
    }
}
