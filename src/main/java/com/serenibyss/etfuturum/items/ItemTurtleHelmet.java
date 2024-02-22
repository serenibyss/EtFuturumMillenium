package com.serenibyss.etfuturum.items;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.items.base.EFMItem;
import com.serenibyss.etfuturum.items.base.EFMItemArmor;
import com.serenibyss.etfuturum.load.enums.EFMArmorMaterial;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemTurtleHelmet extends EFMItemArmor {

    public static final String TEXTURE = EFMTags.MODID + ":textures/models/armor/turtle_layer_1.png";

    public ItemTurtleHelmet() {
        super(EFMArmorMaterial.TURTLE, EntityEquipmentSlot.HEAD, new EFMItem.Settings().translationKey("turtle_helmet"));
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (!player.isInsideOfMaterial(Material.WATER)) {
            player.addPotionEffect(new PotionEffect(Objects.requireNonNull(MobEffects.WATER_BREATHING), 200, 0, false, false));
        }
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return TEXTURE;
    }
}
