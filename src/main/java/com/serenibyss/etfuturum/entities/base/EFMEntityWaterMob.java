package com.serenibyss.etfuturum.entities.base;

import com.serenibyss.etfuturum.load.enums.EFMEnumCreatureAttribute;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EFMEntityWaterMob extends EntityCreature implements IAnimals {
    protected EFMEntityWaterMob(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EFMEnumCreatureAttribute.WATER;
    }

    @Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 1 + this.world.rand.nextInt(3);
    }

    protected void updateAir(int air) {
        if(this.isEntityAlive() && !this.isInWater()) {
            this.setAir(air - 1);
            if(this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0f);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    public void onEntityUpdate() {
        int i = this.getAir();
        super.onEntityUpdate();
        this.updateAir(i);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }
}
