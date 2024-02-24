package com.serenibyss.etfuturum.entities.passive.fish;

import com.google.common.base.Predicate;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.enums.EFMEnumCreatureAttribute;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class EntityPufferfish extends AbstractFish {
    private static final DataParameter<Integer> PUFF_STATE = EntityDataManager.createKey(EntityPufferfish.class, DataSerializers.VARINT);
    private int puffTimer;
    private int deflateTimer;
    private static final Predicate<EntityLivingBase> ENEMY_MATCHER = (enemy) -> {
        if(enemy == null) {
            return false;
        } else if(!(enemy instanceof EntityPlayer) || !((EntityPlayer)enemy).isSpectator() && !((EntityPlayer)enemy).isCreative()) {
            return enemy.getCreatureAttribute() != EFMEnumCreatureAttribute.WATER;
        } else {
            return false;
        }
    };
    private float originalWidth = -1.0f;
    private float originalHeight;

    public EntityPufferfish(World world) {
        super(world);
        setSize(0.7f, 0.7f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PUFF_STATE, 0);
    }

    public int getPuffState() {
        return dataManager.get(PUFF_STATE);
    }

    public void setPuffState(int puffState) {
        dataManager.set(PUFF_STATE, puffState);
        onPuffStateChanged(puffState);
    }

    private void onPuffStateChanged(int state) {
        float f = 1.0F;
        if(state == 1) {
            f = 0.7f;
        } else if (state == 0) {
            f = 0.5f;
        }

        updateSize(f);
    }

    @Override
    protected void setSize(float width, float height) {
        boolean flag = this.originalWidth > 0.0f;
        this.originalWidth = width;
        this.originalHeight = height;
        if(!flag) {
            this.updateSize(1.0F);
        }
    }

    private void updateSize(float scale) {
        super.setSize(this.originalWidth * scale, this.originalHeight * scale);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        this.onPuffStateChanged(this.getPuffState());
        super.notifyDataManagerChange(key);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("PuffState", this.getPuffState());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setPuffState(compound.getInteger("PuffState"));
    }

    @Override
    protected ItemStack getFishBucket() {
        return EFMItems.PUFFERFISH_BUCKET.getItemStack();
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new AIPuff(this));
    }

    @Override
    public void onUpdate() {
        if(isAlive() && !world.isRemote && this.isServerWorld()) {
            if(puffTimer > 0) {
                if(getPuffState() == 0) {
                    playSound(EFMSounds.ENTITY_PUFFER_FISH_BLOW_UP, getSoundVolume(), getSoundPitch());
                    setPuffState(1);
                } else if(puffTimer > 40 && getPuffState() == 1) {
                    playSound(EFMSounds.ENTITY_PUFFER_FISH_BLOW_UP, getSoundVolume(), getSoundPitch());
                    setPuffState(2);
                }

                ++puffTimer;
            } else if (getPuffState() != 0) {
                if(deflateTimer > 60 && getPuffState() == 2) {
                    playSound(EFMSounds.ENTITY_PUFFER_FISH_BLOW_OUT, getSoundVolume(), getSoundPitch());
                    setPuffState(1);
                } else if (deflateTimer > 100 && getPuffState() == 1) {
                    playSound(EFMSounds.ENTITY_PUFFER_FISH_BLOW_OUT, getSoundVolume(), getSoundPitch());
                    setPuffState(0);
                }

                ++deflateTimer;
            }
        }
        super.onUpdate();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isAlive() && getPuffState() > 0) {
            for(EntityMob mob : world.getEntitiesWithinAABB(EntityMob.class, this.getEntityBoundingBox().grow(0.3), ENEMY_MATCHER)) {
                if(mob.isEntityAlive()) {
                    attack(mob);
                }
            }
        }
    }

    private void attack(EntityMob mob) {
        int i = getPuffState();
        if(mob.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(1 + i))) {
            mob.addPotionEffect(new PotionEffect(MobEffects.POISON, 60 * i, 0));
            this.playSound(EFMSounds.ENTITY_PUFFER_FISH_STING, 1.0f, 1.0f);
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        int i = getPuffState();
        if(entityIn instanceof EntityPlayerMP && i > 0 && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(1 + i))) {
            ((EntityPlayerMP)entityIn).connection.sendPacket(new SPacketChangeGameState(9, 0.0f));
            entityIn.addPotionEffect(new PotionEffect(MobEffects.POISON, 60 * i, 0));
            //this.playSound(EFMSounds.ENTITY_PUFFER_FISH_STING, 1.0f, 1.0f);
        }
    }
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return EFMSounds.ENTITY_PUFFER_FISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return EFMSounds.ENTITY_PUFFER_FISH_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return EFMSounds.ENTITY_PUFFER_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return EFMSounds.ENTITY_PUFFER_FISH_FLOP;
    }

    static class AIPuff extends EntityAIBase {
        private final EntityPufferfish puffer;

        public AIPuff(EntityPufferfish fish) {
            puffer = fish;
        }

        @Override
        public boolean shouldExecute() {
            List<EntityLivingBase> list = puffer.world.getEntitiesWithinAABB(EntityLivingBase.class, puffer.getEntityBoundingBox().grow(2.0), EntityPufferfish.ENEMY_MATCHER);
            return !list.isEmpty();
        }

        @Override
        public void startExecuting() {
            puffer.puffTimer = 1;
            puffer.deflateTimer = 0;
        }

        @Override
        public void resetTask() {
            puffer.puffTimer = 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            List<EntityLivingBase> list = puffer.world.getEntitiesWithinAABB(EntityLivingBase.class, puffer.getEntityBoundingBox().grow(2.0), EntityPufferfish.ENEMY_MATCHER);
            return !list.isEmpty();
        }
    }
}
