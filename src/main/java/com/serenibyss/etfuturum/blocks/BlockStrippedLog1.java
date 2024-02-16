package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.blocks.base.EFMBlockRotatedPillar;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

public class BlockStrippedLog1 extends EFMBlockRotatedPillar {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, type -> type.getMetadata() < 4);

    public BlockStrippedLog1() {
        super(new Settings(Material.WOOD).hardness(2.0F).resistance(2.0F).soundType(SoundType.WOOD));
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPlanks.EnumType type = state.getValue(VARIANT);
        return type.getMapColor();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta & 0b0011) % 4));
        IBlockState rotState = super.getStateFromMeta(meta);

        return state.withProperty(AXIS, rotState.getValue(AXIS));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta |= state.getValue(VARIANT).getMetadata();
        meta |= super.getMetaFromState(state);

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, AXIS);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getMetadata());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public void registerModel() {
        Item thisBlock = Item.getItemFromBlock(this);
        ModelLoader.setCustomModelResourceLocation(thisBlock, 0, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "oak_stripped_log"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(thisBlock, 1, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "spruce_stripped_log"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(thisBlock, 2, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "birch_stripped_log"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(thisBlock, 3, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "jungle_stripped_log"), "inventory"));
    }
}
