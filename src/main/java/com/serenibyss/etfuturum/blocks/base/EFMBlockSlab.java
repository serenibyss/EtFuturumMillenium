package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.util.VoxelShape;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

import java.util.Random;

/**
 * You should not extend this class, instead extend:<br>
 * - {@link EFMBlockHalfSlab}<br>
 * - {@link EFMBlockDoubleSlab}
 */
@SuppressWarnings("deprecation")
public abstract class EFMBlockSlab extends EFMBlock {

    public static final PropertyEnum<EnumBlockHalf> HALF = BlockSlab.HALF;
    private static final VoxelShape BOTTOM_SHAPE = createShape(0, 0, 0, 16, 8, 16);
    private static final VoxelShape TOP_SHAPE = createShape(0, 8, 0, 16, 16, 16);

    public EFMBlockSlab(Settings settings) {
        super(settings);
        this.fullBlock = this.isDouble();
        this.setLightOpacity(255);
    }

    public abstract boolean isDouble();

    public abstract EFMBlockHalfSlab getHalfSlab();

    public abstract EFMBlockDoubleSlab getDoubleSlab();

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(getHalfSlab());
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(getHalfSlab());
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (isDouble()) {
            return FULL_BLOCK_AABB;
        } else {
            return state.getValue(HALF) == EnumBlockHalf.TOP ? TOP_SHAPE : BOTTOM_SHAPE;
        }
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        EFMBlockSlab slab = ((EFMBlockSlab) state.getBlock());
        return slab.isDouble() || state.getValue(HALF) == EnumBlockHalf.TOP;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        EFMBlockSlab slab = ((EFMBlockSlab) state.getBlock());
        if (slab.isDouble()) {
            return BlockFaceShape.SOLID;
        } else if (face == EnumFacing.UP && state.getValue(HALF) == EnumBlockHalf.TOP) {
            return BlockFaceShape.SOLID;
        }
        return face == EnumFacing.DOWN && state.getValue(HALF) == EnumBlockHalf.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return isDouble();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (ForgeModContainer.disableStairSlabCulling) {
            return super.doesSideBlockRendering(state, world, pos, face);
        }
        if (state.isOpaqueCube()) {
            return true;
        }
        EnumBlockHalf side = state.getValue(HALF);
        return (side == EnumBlockHalf.TOP && face == EnumFacing.UP) || (side == EnumBlockHalf.BOTTOM && face == EnumFacing.DOWN);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        if (isDouble()) {
            return state;
        }
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? state : state.withProperty(HALF, EnumBlockHalf.TOP);
    }

    @Override
    public int quantityDropped(Random random) {
        return isDouble() ? 2 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return isDouble();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (isDouble()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        } else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
