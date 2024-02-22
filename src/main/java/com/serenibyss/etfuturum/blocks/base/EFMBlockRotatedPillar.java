package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.EtFuturum;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class EFMBlockRotatedPillar extends EFMBlock {
    public EFMBlockRotatedPillar(Settings settings) {
        super(settings);
    }

    public static final PropertyEnum<Axis> AXIS = BlockRotatedPillar.AXIS;

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getPropertyKeys()) {
            if (prop.getName().equals("axis")) {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        if (rot == Rotation.COUNTERCLOCKWISE_90 || rot == Rotation.CLOCKWISE_90) {
            Axis axis = state.getValue(AXIS);
            if (axis == Axis.X) {
                return state.withProperty(AXIS, Axis.Z);
            } else if (axis == Axis.Z) {
                return state.withProperty(AXIS, Axis.X);
            }
        }
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        Axis axis = Axis.Y;
        int rot = meta & 0b1100;

        if (rot == 0b0100) {
            axis = Axis.X;
        } else if (rot == 0b1000) {
            axis = Axis.Z;
        }

        return this.getDefaultState().withProperty(AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        Axis axis = state.getValue(AXIS);

        if (axis == Axis.X) {
            meta |= 0b0100;
        } else if (axis == Axis.Z) {
            meta |= 0b1000;
        }

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(AXIS, facing.getAxis());
    }
}
