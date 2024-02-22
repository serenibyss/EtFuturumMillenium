package com.serenibyss.etfuturum.entities;

import com.serenibyss.etfuturum.entities.passive.EntityTurtle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;

import static com.serenibyss.etfuturum.load.feature.Features.MC13;

public class EFMEntityAI {

    public static void injectEntityAI(EntityLiving entity) {
        if (MC13.turtle.isEnabled()) {
            if (entity instanceof AbstractSkeleton skeleton) {
                skeleton.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(skeleton, EntityTurtle.class, 10, true, false, EntityTurtle.TARGET_DRY_BABY));
            } else if (entity instanceof EntityZombie zombie) {
                zombie.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(zombie, EntityTurtle.class, 10, true, false, EntityTurtle.TARGET_DRY_BABY));
            } else if (entity instanceof EntityOcelot ocelot) {
                ocelot.targetTasks.addTask(1, new EntityAITargetNonTamed<>(ocelot, EntityTurtle.class, false, EntityTurtle.TARGET_DRY_BABY));
            } else if (entity instanceof EntityWolf wolf) {
                wolf.targetTasks.addTask(4, new EntityAITargetNonTamed<>(wolf, EntityTurtle.class, false, EntityTurtle.TARGET_DRY_BABY));
            }
            // todo: Fox, Drowned
        }
    }
}
