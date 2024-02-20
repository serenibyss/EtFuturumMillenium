package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.util.ModIDs;
import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;

public class BlockSeaGrassTall extends BlockDoublePlant implements IFluidloggable {

    public BlockSeaGrassTall() {
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return state;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return EFMBlocks.SEA_GRASS.getItemStack();
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public boolean isFluidloggable(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos) {
        return true;
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public boolean isFluidValid(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public boolean canFluidFlow(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState here, @NotNull EnumFacing side) {
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return Blocks.WATER.getDefaultState().getLightOpacity(world, pos);
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return net.minecraftforge.common.EnumPlantType.Water;
    }
}
