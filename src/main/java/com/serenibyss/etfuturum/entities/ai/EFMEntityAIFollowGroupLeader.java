package com.serenibyss.etfuturum.entities.ai;

import com.serenibyss.etfuturum.entities.passive.fish.AbstractGroupFish;
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
        if (owner.isGroupLeader()) return false;
        if (owner.hasGroupLeader()) return true;
        if (this.size > 0) {
            this.size--;
            return false;
        }

        size = updateSize(owner);
        List<AbstractGroupFish> list = owner.world.getEntitiesWithinAABB(
                owner.getClass(),
                owner.getEntityBoundingBox().grow(8.0, 8.0, 8.0),
                group -> group.canGroupGrow() || !group.hasGroupLeader());

        AbstractGroupFish group = list.stream()
                .filter(AbstractGroupFish::canGroupGrow)
                .findAny()
                .orElse(owner);
        group.splitGroup(list.stream().filter(g -> !g.hasGroupLeader()));
        return owner.hasGroupLeader();
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
        if (navigateTimer-- <= 0) {
            navigateTimer = 10;
            owner.moveToGroupLeader();
        }
    }
}
