package com.serenibyss.etfuturum.items.base;

import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.Nullable;

public abstract class EFMItemArmor extends ItemArmor implements IModelRegister {

    EnumRarity rarity;

    // mark the armor material as nullable to take the null warning out of EFMItems
    @SuppressWarnings("ConstantConditions")
    public EFMItemArmor(@Nullable ArmorMaterial material, EntityEquipmentSlot slot, EFMItem.Settings settings) {
        super(material, -1, slot);
        setMaxStackSize(1);
        CreativeTabs tab = settings.tab;
        if (tab == null) {
            tab = CreativeTabs.COMBAT;
        }
        setCreativeTab(tab);
        rarity = settings.rarity;
        setTranslationKey(settings.translationKey);
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return rarity;
    }
}
