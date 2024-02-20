package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.blocks.base.EFMBlock;
import com.serenibyss.etfuturum.blocks.base.EFMBlockContainer;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import com.serenibyss.etfuturum.util.VoxelShape;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BlockConduit extends EFMBlockContainer {

    private static final VoxelShape SHAPE = EFMBlock.createShape(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

    public BlockConduit() {
        super(new Settings(Material.GLASS, MapColor.DIAMOND)
                .hardness(3.0f)
                .resistance(3.0f)
                .lightValue(15)
                .nonFullCube()
                .nonOpaque()
                .creativeTab(CreativeTabs.MISC)
                .rarity(EnumRarity.RARE)
                .translationKey("conduit"));
    }

    @Override
    public boolean isWaterloggable(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityConduit();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // todo beacon custom name?? is this even meant to be here???
        //if (stack.hasDisplayName()) {
        //    TileEntity tile = world.getTileEntity(pos);
        //    if (tile instanceof TileEntityBeacon beacon) {
        //        beacon.setCustomName(stack.getDisplayName());
        //    }
        //}
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
