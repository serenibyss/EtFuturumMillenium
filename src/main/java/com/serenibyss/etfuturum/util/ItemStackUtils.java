package com.serenibyss.etfuturum.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackUtils {

    public static boolean areItemStacksEqualIgnoreCount(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        }
        if (stackA.isEmpty() || stackB.isEmpty()) {
            return false;
        }

        if (stackA.getItem() != stackB.getItem()) {
            return false;
        }
        if (stackA.getItemDamage() != stackB.getItemDamage()) {
            return false;
        }
        NBTTagCompound tagA = stackA.getTagCompound();
        NBTTagCompound tagB = stackB.getTagCompound();
        if (tagA == null && tagB != null) {
            return false;
        }
        if (tagA != null && tagB == null) {
            return false;
        }
        return (tagA == null || tagA.equals(tagB)) && stackA.areCapsCompatible(stackB);
    }
}
