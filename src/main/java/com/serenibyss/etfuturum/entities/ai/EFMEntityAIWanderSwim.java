package com.serenibyss.etfuturum.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class EFMEntityAIWanderSwim extends EntityAIWander {

    public EFMEntityAIWanderSwim(EntityCreature creatureIn, double speedIn, int chance) {
        super(creatureIn, speedIn, chance);
    }

    @Nullable
    @Override
    protected Vec3d getPosition() {
        Vec3d vec;
        int i = 0;
        do {
            vec = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
            if (vec != null) {
                BlockPos pos = new BlockPos(vec);
                if (entity.world.getBlockState(pos).getBlock().isPassable(entity.world, pos)) {
                    return vec;
                }
            }
        } while (i++ < 10);
        return vec;
    }
}
