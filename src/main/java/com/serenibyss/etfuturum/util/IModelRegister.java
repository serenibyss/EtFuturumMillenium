package com.serenibyss.etfuturum.util;

import com.serenibyss.etfuturum.EtFuturum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IModelRegister {

    default void registerModel() {
        if (this instanceof Item item) {
            EtFuturum.proxy.registerItemRenderer(item, 0, "inventory");
        } else if (this instanceof Block block) {
            EtFuturum.proxy.registerItemRenderer(Item.getItemFromBlock(block), 0, "inventory");
        }
    }
}
