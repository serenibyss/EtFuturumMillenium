package com.serenibyss.etfuturum.tiles;

import com.serenibyss.etfuturum.blocks.BlockBarrel;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.containers.ContainerBarrel;
import com.serenibyss.etfuturum.sounds.EtFuturumSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBarrel extends TileEntityLockableLoot {

    public int playerUsingCount = 0;
    public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        IBlockState state = world.getBlockState(pos);
        if (type > 0 && !state.getValue(BlockBarrel.OPEN)) {
            world.setBlockState(pos, state.withProperty(BlockBarrel.OPEN, true), 3);
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.playSound(null, pos, EtFuturumSounds.BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else {
            world.setBlockState(pos, state.withProperty(BlockBarrel.OPEN, false), 3);
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.playSound(null, pos, EtFuturumSounds.BARREL_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        playerUsingCount++;
        world.addBlockEvent(pos, EFMBlocks.BARREL.getBlock(), 1, this.playerUsingCount);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        playerUsingCount--;
        world.addBlockEvent(pos, EFMBlocks.BARREL.getBlock(), 1, this.playerUsingCount);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player) {
        return new ContainerBarrel(playerInventory, this, player);
    }

    @Override
    public String getGuiID() {
        return "etfuturum:barrel";
    }

    @Override
    public String getName() {
        return "container.barrel";
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        if (!checkLootAndRead(compound)) ItemStackHelper.loadAllItems(compound, inventory);
    }
}
