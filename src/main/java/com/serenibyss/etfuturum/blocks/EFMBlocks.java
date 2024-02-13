package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.base.EFMBlock;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.load.feature.Features;
import com.serenibyss.etfuturum.tiles.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

public enum EFMBlocks {

    BARREL(Features.MC14.barrel, "barrel", new BlockBarrel(), TileEntityBarrel.class),

    ;

    private final Feature feature;
    private final String myName;
    private final EFMBlock myBlock;
    @Nullable
    private final Class<? extends TileEntity> myTile;

    EFMBlocks(Feature feature, String myName, EFMBlock myBlock) {
        this(feature, myName, myBlock, null);
    }

    EFMBlocks(Feature feature, String myName, EFMBlock myBlock, @Nullable Class<? extends TileEntity> myTile) {
        this.feature = feature;
        this.myName = myName;
        this.myBlock = myBlock;
        this.myTile = myTile;
    }

    public EFMBlock getBlock() {
        return myBlock;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        for (EFMBlocks value : values()) {
            if (value.isEnabled()) {
                if (value.myBlock != null) {
                    value.myBlock.setRegistryName(new ResourceLocation(EFMTags.MODID, value.myName));
                    r.register(value.myBlock);
                }
                if (value.myTile != null) {
                    GameRegistry.registerTileEntity(value.myTile, new ResourceLocation(EFMTags.MODID, value.myName));
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        for (EFMBlocks value : values()) {
            if (value.isEnabled()) {
                if (value.myBlock != null) {
                    ItemBlock ib = new ItemBlock(value.myBlock);
                    ib.setRegistryName(new ResourceLocation(EFMTags.MODID, value.myName));
                    r.register(ib);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (EFMBlocks value : values()) {
            if (value.isEnabled()) {
                if (value.myBlock != null) {
                    value.myBlock.registerModel();
                }
            }
        }
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }
}
