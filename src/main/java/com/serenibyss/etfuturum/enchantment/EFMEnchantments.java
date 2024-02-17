package com.serenibyss.etfuturum.enchantment;

import com.serenibyss.etfuturum.EFMTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class EFMEnchantments {

    public static Enchantment RIPTIDE = new EnchantmentRiptide(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND).setRegistryName(EFMTags.MODID, "riptide");
    public static Enchantment LOYALTY = new EnchantmentLoyalty(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND).setRegistryName(EFMTags.MODID, "loyalty");
    public static Enchantment CHANNELING = new EnchantmentChanneling(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND).setRegistryName(EFMTags.MODID, "channeling");
    public static Enchantment IMPALING = new EnchantmentImpaling(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND).setRegistryName(EFMTags.MODID, "impaling");

    @SubscribeEvent
    public static void initializeEnchants(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> r = event.getRegistry();

        r.register(RIPTIDE);
        r.register(LOYALTY);
        r.register(CHANNELING);
        r.register(IMPALING);
    }
}
