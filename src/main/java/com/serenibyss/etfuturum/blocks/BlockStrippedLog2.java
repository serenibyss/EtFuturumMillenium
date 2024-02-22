package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EFMTags;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockStrippedLog2 extends EFMBlockRotatedPillar {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, type -> type.getMetadata() >= 4);

    public BlockStrippedLog2() {
        super(new Settings(Material.WOOD)
                .hardness(2.0F)
                .resistance(2.0F)
                .soundType(SoundType.WOOD)
                .creativeTab(CreativeTabs.BUILDING_BLOCKS)
                .hasItemSubtypes()
                .translationKey("stripped_log_2"));
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPlanks.EnumType type = state.getValue(VARIANT);
        return type.getMapColor();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(AXIS, facing.getAxis());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta % 4) + 4));
        IBlockState rotState = super.getStateFromMeta(meta);

        return state.withProperty(AXIS, rotState.getValue(AXIS));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta |= state.getValue(VARIANT).getMetadata() - 4;
        meta |= super.getMetaFromState(state);

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, AXIS);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getMetadata() - 4);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata() - 4;
    }

    @Override
    public void registerModel() {
        Item thisBlock = Item.getItemFromBlock(this);
        ModelLoader.setCustomModelResourceLocation(thisBlock, 0, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "acacia_stripped_log"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(thisBlock, 1, new ModelResourceLocation(new ResourceLocation(EFMTags.MODID, "dark_oak_stripped_log"), "inventory"));
    }

    @Override
    public String getTranslationKey(int meta) {
        IBlockState state = this.getStateFromMeta(meta);
        return String.format("tile.%s_stripped_log", state.getValue(VARIANT).getTranslationKey());
    }
}
