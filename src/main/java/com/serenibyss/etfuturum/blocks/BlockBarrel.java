package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.blocks.base.EFMBlockDirectional;
import com.serenibyss.etfuturum.client.EFMGuiIDs;
import com.serenibyss.etfuturum.tiles.TileEntityBarrel;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BlockBarrel extends EFMBlockDirectional implements ITileEntityProvider {

    public static final PropertyBool OPEN = PropertyBool.create("open");

    public BlockBarrel() {
        super(new Settings(Material.WOOD)
                .strength(2.5F)
                .soundType(SoundType.WOOD)
                .creativeTab(CreativeTabs.DECORATIONS)
                .translationKey("barrel"));
        setDefaultState(getBlockState().getBaseState().withProperty(OPEN, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(EtFuturum.INSTANCE, EFMGuiIDs.GUI_BARREL, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityBarrel barrel) {
            InventoryHelper.dropInventoryItems(world, pos, barrel);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tile = world.getTileEntity(pos);
        return tile != null && tile.receiveClientEvent(id, param);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = super.getMetaFromState(state);
        if (state.getValue(OPEN)) meta |= 8;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBarrel();
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }
}
