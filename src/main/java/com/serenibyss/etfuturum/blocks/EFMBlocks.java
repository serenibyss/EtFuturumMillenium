package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.base.*;
import com.serenibyss.etfuturum.blocks.base.EFMBlock.Settings;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.tiles.TileEntityBarrel;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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
    STRIPPED_LOG_1(MC13.stripping, "stripped_log_1", new BlockStrippedLog1()),
    STRIPPED_LOG_2(MC13.stripping, "stripped_log_2", new BlockStrippedLog2()),
    TURTLE_EGG(MC13.turtle, "turtle_egg", new BlockTurtleEgg()),
    // SEA_GRASS(MC13.turtle, "sea_grass", new BlockSeaGrass()),
    // SEA_GRASS_TALL(MC13.turtle, "sea_grass_tall", new BlockSeaGrassTall()),

    BARREL(MC14.barrel, "barrel", new BlockBarrel(), TileEntityBarrel.class),
    STONECUTTER(MC14.stonecutter, "stonecutter", new BlockStonecutter()),
    SMOOTH_STONE(MC14.smoothStone, "smooth_stone", new EFMBlock(new Settings(Material.ROCK, MapColor.STONE).hardness(2.0f).resistance(6.0f).creativeTab(CreativeTabs.BUILDING_BLOCKS).translationKey("smooth_stone"))),

    STONE_STAIRS(MISC.newStairs, "stone_stairs", new EFMBlockStairs(Blocks.STONE, "stone_stairs")),
    GRANITE_STAIRS(MISC.newStairs, "granite_stairs", new EFMBlockStairs(Blocks.STONE, 1, "granite_stairs")),
    POLISHED_GRANITE_STAIRS(MISC.newStairs, "polished_granite_stairs", new EFMBlockStairs(Blocks.STONE, 2, "polished_granite_stairs")),
    DIORITE_STAIRS(MISC.newStairs, "diorite_stairs", new EFMBlockStairs(Blocks.STONE, 3, "diorite_stairs")),
    POLISHED_DIORITE_STAIRS(MISC.newStairs, "polished_diorite_stairs", new EFMBlockStairs(Blocks.STONE, 4, "polished_diorite_stairs")),
    ANDESITE_STAIRS(MISC.newStairs, "andesite_stairs", new EFMBlockStairs(Blocks.STONE, 5, "andesite_stairs")),
    POLISHED_ANDESITE_STAIRS(MISC.newStairs, "polished_andesite_stairs", new EFMBlockStairs(Blocks.STONE, 6, "polished_andesite_stairs")),

    STONE_SLAB(MISC.newSlabs, "stone_slab", SlabFactory.STONE.getHalf()),
    STONE_DOUBLE_SLAB(MISC.newSlabs, "stone_double_slab", SlabFactory.STONE.getDouble()),
    GRANITE_SLAB(MISC.newSlabs, "granite_slab", SlabFactory.GRANITE.getHalf()),
    GRANITE_DOUBLE_SLAB(MISC.newSlabs, "granite_double_slab", SlabFactory.GRANITE.getDouble()),

    STONE_BRICK_WALL(MISC.newWalls, "stone_brick_wall", new EFMBlockWall(Blocks.STONEBRICK, "stone_brick_wall")),
    GRANITE_WALL(MISC.newWalls, "granite_wall", new EFMBlockWall(Blocks.STONE, "granite_wall")),
    DIORITE_WALL(MISC.newWalls, "diorite_wall", new EFMBlockWall(Blocks.STONE, "diorite_wall")),
    ANDESITE_WALL(MISC.newWalls, "andesite_wall", new EFMBlockWall(Blocks.STONE, "andesite_wall")),

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
                } else {
                    ib = new EFMItemBlock(value.myBlock);
                }
                if (ib != null) {
                    ib.setRegistryName(new ResourceLocation(EFMTags.MODID, value.myName));
                    r.register(ib);
                }
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
