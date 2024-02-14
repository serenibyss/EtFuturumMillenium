package com.serenibyss.etfuturum.containers.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Modern MC's concept of "Data Slots" for data synchronization.
 */
public abstract class EFMContainer extends Container {

    private final List<IntReferenceHolder> dataSlots = new ArrayList<>();

    public abstract ITextComponent getTitle();

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.dataSlots.size(); i++) {
            IntReferenceHolder holder = this.dataSlots.get(i);
            if (holder.checkAndClearUpdateFlag()) {
                for (IContainerListener listener : this.listeners) {
                    listener.sendWindowProperty(this, i, holder.get());
                }
            }
        }
    }

    protected IntReferenceHolder addDataSlot(IntReferenceHolder holder) {
        this.dataSlots.add(holder);
        return holder;
    }

    // this is what handles the window property sync
    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        this.dataSlots.get(id).set(data);
    }

    protected static boolean isWithinUsableDistance(World world, BlockPos pos, EntityPlayer player, Block targetBlock) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != targetBlock) {
            return false;
        }

        return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }
}
