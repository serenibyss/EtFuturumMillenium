package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMArmorMaterial {

    public static final ItemArmor.ArmorMaterial TURTLE = register(MC13.turtle, "turtle", 25, new int[]{2, 5, 6, 2}, 9, EFMSounds.ITEM_ARMOR_EQUIP_TURTLE, 0.0f, EFMItems.SCUTE.getItemStack());

    private static ItemArmor.ArmorMaterial register(Feature feature, String name, int durability,
                                                    int[] damageReduction, int enchantability,
                                                    SoundEvent equipSound, float toughness, ItemStack repairStack) {
        if (!feature.isEnabled()) {
            return null;
        }
        ItemArmor.ArmorMaterial mat = EnumHelper.addEnum(ItemArmor.ArmorMaterial.class, name,
                new Class<?>[]{String.class, int.class, int[].class, int.class, SoundEvent.class, float.class},
                name, durability, damageReduction, enchantability, equipSound, toughness);
        if (mat != null && repairStack != null && !repairStack.isEmpty()) {
            mat.setRepairItem(repairStack.copy());
        }
        return mat;
    }
}
