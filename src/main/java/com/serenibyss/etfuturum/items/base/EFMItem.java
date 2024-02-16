package com.serenibyss.etfuturum.items.base;

import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;

public class EFMItem extends Item implements IModelRegister {

    EnumRarity rarity;

    public EFMItem(Settings settings) {
        setMaxStackSize(settings.maxCount);
        setMaxDamage(settings.maxDamage);
        setCreativeTab(settings.tab);
        rarity = settings.rarity;
        setTranslationKey(settings.translationKey);
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return rarity;
    }

    public static class Settings {

        int maxCount = 64;
        int maxDamage;
        CreativeTabs tab;
        EnumRarity rarity = EnumRarity.COMMON;
        String translationKey;

        public Settings() {}

        public Settings maxCount(int count) {
            maxCount = count;
            return this;
        }

        public Settings maxDamage(int damage) {
            maxCount = damage;
            return this;
        }

        public Settings creativeTab(CreativeTabs tab) {
            this.tab = tab;
            return this;
        }

        public Settings setRarity(EnumRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Settings translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }
    }
}
