package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.EtFuturum;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@SuppressWarnings("deprecation")
public abstract class EFMBlockHalfSlab extends EFMBlockSlab {

    public EFMBlockHalfSlab(Settings settings) {
        super(settings);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(HALF, EnumBlockHalf.BOTTOM));
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public EFMBlockHalfSlab getHalfSlab() {
        return this;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HALF, meta == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HALF) == EnumBlockHalf.TOP) {
            return 1;
        }
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public boolean isWaterloggable(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void registerModel() {
        Item item = Item.getItemFromBlock(this);
        EtFuturum.proxy.registerItemRenderer(item, 0, "inventory");
        EtFuturum.proxy.registerItemRenderer(item, 1, "inventory");
    }
}
