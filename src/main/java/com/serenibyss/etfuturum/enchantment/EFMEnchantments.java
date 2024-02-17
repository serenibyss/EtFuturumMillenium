package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMEnchantments {

    public static Enchantment RIPTIDE;
    public static Enchantment LOYALTY;
    public static Enchantment CHANNELING;
    public static Enchantment IMPALING;

    @SubscribeEvent
    public static void initializeEnchants(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> r = event.getRegistry();

        RIPTIDE = register(MC13.trident, r, "riptide", new EnchantmentRiptide(Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        LOYALTY = register(MC13.trident, r, "loyalty", new EnchantmentLoyalty(Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
        CHANNELING = register(MC13.trident, r, "channeling", new EnchantmentChanneling(Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
        IMPALING = register(MC13.trident, r, "impaling", new EnchantmentImpaling(Rarity.RARE, EntityEquipmentSlot.MAINHAND));
    }

    private static Enchantment register(Feature feature, IForgeRegistry<Enchantment> registry, String name, Enchantment enchantment) {
        if (!feature.isEnabled()) {
            return null;
        }
        enchantment.setRegistryName(EFMTags.MODID, name);
        registry.register(enchantment);
        return enchantment;
    }
}
