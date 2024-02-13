package com.serenibyss.etfuturum.items;

import com.serenibyss.etfuturum.EtFuturum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.ApiStatus;

public class EFMItem extends Item {

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

    @ApiStatus.Internal
    public void registerModel() {
        EtFuturum.proxy.registerItemRenderer(this, 0, "inventory");
    }

    public static class Settings {

        int maxCount = 64;
        int maxDamage;
        CreativeTabs tab;
        EnumRarity rarity;
        String translationKey;

        public Settings() {
            rarity = EnumRarity.COMMON;
        }

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

        public Settings translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }
    }
}
