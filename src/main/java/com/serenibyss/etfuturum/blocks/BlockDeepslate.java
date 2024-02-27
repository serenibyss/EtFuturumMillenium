package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.blocks.base.IMultiItemBlock;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Random;

public class BlockDeepslate extends Block implements IModelRegister, IMultiItemBlock {
    public static final PropertyEnum<BlockDeepslate.EnumType> VARIANT = PropertyEnum.<BlockDeepslate.EnumType>create("variant", BlockDeepslate.EnumType.class);

    public BlockDeepslate() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.COBBLED_DEEPSLATE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + "." + EnumType.COBBLED_DEEPSLATE.getTranslationKey() + ".name");
    }



    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return ((BlockDeepslate.EnumType)state.getValue(VARIANT)).getMapColor();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(EFMBlocks.DEEPSLATE.getBlock());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockDeepslate.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for(BlockDeepslate.EnumType type : BlockDeepslate.EnumType.values()) {
            items.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockDeepslate.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockDeepslate.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public void registerModel() {
        Item itemBlock = Item.getItemFromBlock(this);
        ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "cobbled_deepslate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 1, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "chiseled_deepslate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 2, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "polished_deepslate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 3, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "deepslate_bricks"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 4, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "cracked_deepslate_bricks"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 5, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "deepslate_tiles"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBlock, 6, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "cracked_deepslate_tiles"), "inventory"));
    }

    @Override
    public boolean getHasItemSubtypes() {
        return true;
    }

    @Override
    public String getTranslationKey(int meta) {
        IBlockState state = this.getStateFromMeta(meta);
        return String.format("tile.%s", state.getValue(VARIANT).getTranslationKey());
    }

    public static enum EnumType implements IStringSerializable {
        COBBLED_DEEPSLATE(0, MapColor.OBSIDIAN, "cobbled_deepslate", true),
        CHISELED_DEEPSLATE(1, MapColor.OBSIDIAN, "chiseled_deepslate", false),
        POLISHED_DEEPSLATE(2, MapColor.OBSIDIAN, "polished_deepslate", false),
        DEEPSLATE_BRICKS(3, MapColor.OBSIDIAN, "deepslate_bricks", false),
        CRACKED_DEEPSLATE_BRICKS(4, MapColor.OBSIDIAN, "cracked_deepslate_bricks", false),
        DEEPSLATE_TILES(5, MapColor.OBSIDIAN, "deepslate_tiles", false),
        CRACKED_DEEPSLATE_TILES(6, MapColor.OBSIDIAN, "cracked_deepslate_tiles", false);

        private static final BlockDeepslate.EnumType[] META_LOOPUP = new BlockDeepslate.EnumType[values().length];

        private final int meta;
        private final String name;
        private final String translationKey;
        private final MapColor mapColor;
        private final boolean isNatural;

        private EnumType(int meta, MapColor color, String name, boolean natural) {
            this(meta, color, name, name, natural);
        }

        private EnumType(int meta, MapColor color, String name, String translation, boolean natural) {
            this.meta = meta;
            this.name = name;
            this.translationKey = translation;
            this.mapColor = color;
            this.isNatural = natural;
        }

        public int getMetadata() {
            return this.meta;
        }

        public MapColor getMapColor() {
            return this.mapColor;
        }

        public String toString() {
            return this.name;
        }

        public static BlockDeepslate.EnumType byMetadata(int meta) {
            if(meta < 0 || meta >= META_LOOPUP.length) {
                meta = 0;
            }
            return META_LOOPUP[meta];
        }

        public String getName() {
            return this.name;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }

        public boolean isNatural() {
            return this.isNatural;
        }

        static {
            for(BlockDeepslate.EnumType type : values()) {
                META_LOOPUP[type.getMetadata()] = type;
            }
        }
    }
}
