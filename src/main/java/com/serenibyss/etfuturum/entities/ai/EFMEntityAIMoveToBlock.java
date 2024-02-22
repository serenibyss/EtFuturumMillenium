package com.serenibyss.etfuturum.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Extension of {@link net.minecraft.entity.ai.EntityAIMoveToBlock} which allows for an overridable timeoutCounter check. */
public abstract class EFMEntityAIMoveToBlock extends EntityAIBase {

    private final EntityCreature creature;
    private final double movementSpeed;
    /** Controls task execution delay */
    protected int runDelay;
    public int timeoutCounter;
    private int maxStayTicks;
    /** Block to move to */
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    private boolean isAboveDestination;
    private final int searchLength;

    public EFMEntityAIMoveToBlock(EntityCreature creature, double speedIn, int length) {
        this.creature = creature;
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            this.runDelay--;
            return false;
        }
        this.runDelay = 200 + this.creature.getRNG().nextInt(200);
        return this.searchForDestination();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        if (this.creature.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
            this.isAboveDestination = false;
            this.timeoutCounter++;

            if (this.shouldMove()) {
                this.creature.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            this.timeoutCounter--;
        }
    }

    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    /**
     * Searches and sets new destination block and returns true if a suitable block
     * (specified in {@link #shouldMoveTo(World, BlockPos)}) can be found.
     */
    private boolean searchForDestination() {
        BlockPos pos = new BlockPos(this.creature);

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < this.searchLength; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos offsetPos = pos.add(i1, k - 1, j1);

                        if (this.creature.isWithinHomeDistanceFromPosition(offsetPos) && this.shouldMoveTo(this.creature.world, offsetPos)) {
                            this.destinationBlock = offsetPos;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean shouldMove() {
        return this.timeoutCounter % 40 == 0;
    }

    /**
     * Return true to set given position as destination
     */
    protected abstract boolean shouldMoveTo(World worldIn, BlockPos pos);
}
