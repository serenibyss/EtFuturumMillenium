package com.serenibyss.etfuturum.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class EFMEnchantments {

    public static Enchantment RIPTIDE = new EnchantmentRiptide(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND);
    public static Enchantment LOYALTY = new EnchantmentLoyalty(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND);
    public static Enchantment CHANNELING = new EnchantmentChanneling(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND);
    public static Enchantment IMPALING = new EnchantmentImpaling(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND);

    @SubscribeEvent
    public static void initializeEnchants(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> r = event.getRegistry();

        r.register(RIPTIDE);
    }
}
