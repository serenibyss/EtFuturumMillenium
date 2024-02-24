package com.serenibyss.etfuturum.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class EFMEntityAIWanderSwim extends EntityAIWander {
    public EFMEntityAIWanderSwim(EntityCreature creatureIn, double speedIn, int chance) {
        super(creatureIn, speedIn, chance);
    }

    @Nullable
    @Override
    protected Vec3d getPosition() {
        Vec3d vec = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

        for(int i = 0; vec != null && !this.entity.world.getBlockState(new BlockPos(vec)).getBlock().isPassable(entity.world, new BlockPos(vec)) && i++ < 10; vec = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7)) {
            ;
        }

        return vec;
    }
}
