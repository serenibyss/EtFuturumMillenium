package com.serenibyss.etfuturum.entities.passive.fish;

import com.serenibyss.etfuturum.entities.ai.EFMEntityAIFollowGroupLeader;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractGroupFish extends AbstractFish {

    private AbstractGroupFish groupLeader;
    private int size = 1;

    public AbstractGroupFish(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(5, new EFMEntityAIFollowGroupLeader(this));
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return this.getMaxGroupSize();
    }

    public int getMaxGroupSize() {
        return super.getMaxSpawnedInChunk();
    }

    protected boolean isAlive() {
        return !hasGroupLeader();
    }

    public boolean hasGroupLeader() {
        return groupLeader != null && groupLeader.isEntityAlive();
    }

    public AbstractGroupFish addFish(AbstractGroupFish groupLeaderIn) {
        this.groupLeader = groupLeaderIn;
        groupLeaderIn.increaseGroupSize();
        return groupLeaderIn;
    }

    public void leaveGroup() {
        groupLeader.decreaseGroupSize();
        groupLeader = null;
    }

    private void increaseGroupSize() {
        ++this.size;
    }

    private void decreaseGroupSize() {
        --this.size;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && size < this.getMaxGroupSize();
    }

    public boolean isGroupLeader() {
        return size > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.getDistanceSq(this.groupLeader) <= 121.0;
    }

    public void moveToGroupLeader() {
        if(hasGroupLeader()) {
            getNavigator().tryMoveToEntityLiving(this.groupLeader, 1.0);
        }
    }

    public void splitGroup(Stream<AbstractGroupFish> initGroup) {
        initGroup.limit(this.getMaxGroupSize() - this.size)
                .filter(group -> group != this)
                .forEach(newGroup -> newGroup.addFish(this));
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        super.onInitialSpawn(difficulty, livingdata);
        if (livingdata == null) {
            livingdata = new AbstractGroupFish.GroupData(this);
        } else {
            this.addFish(((GroupData)livingdata).group);
        }

        return livingdata;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.isGroupLeader() && this.world.rand.nextInt(200) == 1) {
            List<AbstractFish> list = world.getEntitiesWithinAABB(this.getClass(), this.getEntityBoundingBox().grow(8.0, 8.0, 8.0));
            if (list.size() <= 1) {
                size = 1;
            }
        }
    }

    public static class GroupData implements IEntityLivingData {

        public final AbstractGroupFish group;

        public GroupData(AbstractGroupFish group) {
            this.group = group;
        }
    }
}
