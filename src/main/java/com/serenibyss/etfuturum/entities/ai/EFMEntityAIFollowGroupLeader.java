package com.serenibyss.etfuturum.entities.ai;

import com.google.common.base.Predicate;
import com.serenibyss.etfuturum.entities.passive.fish.AbstractGroupFish;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;

public class EFMEntityAIFollowGroupLeader extends EntityAIBase {
    private final AbstractGroupFish owner;
    private int navigateTimer;
    private int size;

    public EFMEntityAIFollowGroupLeader(AbstractGroupFish owner) {
        this.owner = owner;
        this.size = updateSize(owner);
    }

    protected int updateSize(AbstractGroupFish owner) {
        return 200 + owner.getRNG().nextInt(200) % 20;
    }


    @Override
    public boolean shouldExecute() {
        if(owner.isGroupLeader()) {
            return false;
        } else if (owner.hasGroupLeader()) {
            return true;
        } else if (this.size > 0) {
            --this.size;
            return false;
        } else {
            size = updateSize(owner);
            Predicate<AbstractGroupFish> predicate = (group) -> {
                return group.canGroupGrow() || !group.hasGroupLeader();
            };
            List<AbstractGroupFish> list = owner.world.getEntitiesWithinAABB(owner.getClass(), owner.getEntityBoundingBox().grow(8.0, 8.0, 8.0), predicate);
            AbstractGroupFish g = list.stream().filter(AbstractGroupFish::canGroupGrow).findAny().orElse(owner);
            g.splitGroup(list.stream().filter((group2) -> {
                return !group2.hasGroupLeader();
            }));
            return owner.hasGroupLeader();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return owner.hasGroupLeader() && owner.inRangeOfGroupLeader();
    }

    @Override
    public void startExecuting() {
        navigateTimer = 0;
    }

    @Override
    public void resetTask() {
        owner.leaveGroup();
    }

    @Override
    public void updateTask() {
        if(--navigateTimer <= 0) {
            navigateTimer = 10;
            owner.moveToGroupLeader();
        }
    }
}
