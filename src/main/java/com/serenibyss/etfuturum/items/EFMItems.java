package com.serenibyss.etfuturum.items;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

public enum EFMItems {

    ;

    public final Feature feature;
    public final String myName;
    public final EFMItem myItem;

    EFMItems(Feature feature, String myName, EFMItem myItem) {
        this.feature = feature;
        this.myName = myName;
        this.myItem = myItem;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }

    @Nullable
    public EFMItem getItem() {
        if (isEnabled()) {
            return myItem;
        }
        return null;
    }

    public ItemStack getItemStack() {
        return getItemStack(1, 0);
    }

    public ItemStack getItemStack(int count) {
        return getItemStack(count, 0);
    }

    public ItemStack getItemStack(int count, int meta) {
        if (isEnabled()) {
            return new ItemStack(myItem, count, meta);
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onRegisterItem(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        for (EFMItems value : values()) {
            if (value.isEnabled()) {
                r.register(value.myItem);
            }
        }
    }

    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        for (EFMItems value : values()) {
            if (value.isEnabled()) {
                value.myItem.registerModel();
            }
        }
    }
}