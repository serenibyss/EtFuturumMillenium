package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.blocks.base.EFMBlockHorizontal;
import com.serenibyss.etfuturum.client.EFMGuiIDs;
import com.serenibyss.etfuturum.util.VoxelShape;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockStonecutter extends EFMBlockHorizontal {

    private static final VoxelShape SHAPE = createShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

    public BlockStonecutter() {
        super(new Settings(Material.ROCK)
                .hardness(3.5F)
                .resistance(3.5F)
                .nonFullCube().nonOpaque()
                .creativeTab(CreativeTabs.DECORATIONS)
                .translationKey("stonecutter"));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(EtFuturum.INSTANCE, EFMGuiIDs.GUI_STONECUTTER, world, pos.getX(), pos.getY(), pos.getZ());
            // todo stats
            // player.addStat(EFMStats.INTERACT_WITH_STONECUTTER);
        }
        return true;
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return false;
    }
}
