package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.base.*;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.tiles.TileEntityBarrel;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public enum EFMBlocks {

    CONDUIT(MC13.conduit, "conduit", new BlockConduit(), TileEntityConduit.class),

    BARREL(MC14.barrel, "barrel", new BlockBarrel(), TileEntityBarrel.class),
    STONECUTTER(MC14.stonecutter, "stonecutter", new BlockStonecutter()),

    STONE_STAIRS(MISC.newStairs, "stone_stairs", new EFMBlockStairs(Blocks.STONE, "stone_stairs")),

    STONE_SLAB(MISC.newSlabs, "stone_slab", SlabFactory.STONE_SLAB.getHalfSlab()),
    STONE_DOUBLE_SLAB(MISC.newSlabs, "stone_double_slab", SlabFactory.STONE_SLAB.getDoubleSlab()),

    STRIPPED_LOG_1(MC13.stripping, "stripped_log_1", new BlockStrippedLog1()),
    STRIPPED_LOG_2(MC13.stripping, "stripped_log_2", new BlockStrippedLog2()),

    ;

    private final Feature feature;
    private final String myName;
    private final Block myBlock;
    @Nullable
    private final Class<? extends TileEntity> myTile;

    EFMBlocks(Feature feature, String myName, Block myBlock) {
        this(feature, myName, myBlock, null);
    }

    EFMBlocks(Feature feature, String myName, Block myBlock, @Nullable Class<? extends TileEntity> myTile) {
        this.feature = feature;
        this.myName = myName;
        this.myBlock = myBlock;
        this.myTile = myTile;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }

    @Nullable
    public Block getBlock() {
        if (isEnabled()) {
            return myBlock;
        }
        return null;
    }

    @Nullable
    public Item getItem() {
        if (isEnabled()) {
            return ItemBlock.getItemFromBlock(myBlock);
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
            return new ItemStack(myBlock, count, meta);
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        for (EFMBlocks value : values()) {
            if (value.isEnabled()) {
                value.myBlock.setRegistryName(new ResourceLocation(EFMTags.MODID, value.myName));
                r.register(value.myBlock);
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
                ItemBlock ib;
                if (value.myBlock instanceof EFMBlockSlab slab) {
                    if (slab instanceof EFMBlockDoubleSlab) {
                        ib = null;
                    } else {
                        ib = new EFMItemSlab(slab.getHalfSlab(), slab.getDoubleSlab());
                    }
                } else if (value.myBlock instanceof EFMBlock efmBlock) {
                    ib = new EFMItemBlock(efmBlock);
                    if (efmBlock.hasItemSubtypes()) {
                        ib.setHasSubtypes(true);
                    }
                } else {
                    ib = new ItemBlock(value.myBlock);
                }
                if (ib != null) {
                    ib.setRegistryName(new ResourceLocation(EFMTags.MODID, value.myName));
                    r.register(ib);
                }
                // todo IMultiItemBlock
            }
        }
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (EFMBlocks value : values()) {
            if (value.isEnabled() && value.myBlock instanceof IModelRegister block) {
                block.registerModel();
            }
        }
    }
}
