package com.serenibyss.etfuturum.entities.monster;

import com.serenibyss.etfuturum.entities.base.EntityFlyingMob;
import com.serenibyss.etfuturum.load.config.ConfigEntities;
import com.serenibyss.etfuturum.loot.EFMLootTables;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EntityPhantom extends EntityFlyingMob implements IMob {

    private Vec3d targetVec;
    private BlockPos altitudePos;
    private AttackPhase attackPhase;

    public EntityPhantom(World worldIn) {
        super(worldIn);
        this.targetVec = Vec3d.ZERO;
        this.altitudePos = BlockPos.ORIGIN;
        this.attackPhase = AttackPhase.CIRCLE;
        this.experienceValue = 5;
        this.setSize(0.9f, 0.5f);
        this.moveHelper = new MoveHelper(this);

    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ConfigEntities.phantomDamage);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new AIPickAttack());
        this.tasks.addTask(2, new AISweepAttack());
        this.tasks.addTask(3, new AIOrbitPoint());
        this.targetTasks.addTask(1, new AIAttackPlayer());
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.35f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.world.isRemote) {
            float r1 = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted) * 0.13f + (float)Math.PI);
            float r2 = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted + 1) * 0.13f + (float)Math.PI);
            if(r1 > 0.0f && r2 <= 0.0f) {
                this.world.playSound(this.posX, this.posY, this.posZ, EFMSounds.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.rand.nextFloat() * 0.05f, 0.95F + this.rand.nextFloat() * 0.05f, false);
            }

            int i = 0; // todo phantom size?
            float f2 = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180.0f)) * (1.3f + 0.21f * (float) i);
            float f3 = MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180.0f)) * (1.3f + 0.21f * (float) i);
            float f4 = (0.3f + r1 * 0.45f) * ((float) i * 0.2f + 1.0f);
            this.world.spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX + (double) f2, this.posY + (double) f4, this.posZ + (double) f3, 0, 0, 0);
            this.world.spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX - (double) f2, this.posY + (double) f4, this.posZ - (double) f3, 0, 0, 0);
        }

        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            world.removeEntity(this);
        }
    }

    @Override
    public void onLivingUpdate() {
        if(this.isInDaylight()) {
            this.setFire(8);
        }
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.altitudePos = (new BlockPos(this)).up(5);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("AX")) {
            this.altitudePos = new BlockPos(compound.getInteger("AX"), compound.getInteger("AY"), compound.getInteger("AZ"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("AX", this.altitudePos.getX());
        compound.setInteger("AY", this.altitudePos.getY());
        compound.setInteger("AZ", this.altitudePos.getZ());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return EFMSounds.ENTITY_PHANTOM_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return EFMSounds.ENTITY_PHANTOM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return EFMSounds.ENTITY_PHANTOM_DEATH;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return EFMLootTables.ENTITIES_PHANTOM;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        return true;
    }

    protected boolean isInDaylight() {
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
            return f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(blockpos);
        }

        return false;
    }

    class AIAttackPlayer extends EntityAIBase {

        private int cooldownTimer;

        private AIAttackPlayer() {
            this.cooldownTimer = 20;
        }

        @Override
        public boolean shouldExecute() {
            if(this.cooldownTimer > 0) {
                --this.cooldownTimer;
            } else {
                this.cooldownTimer = 60;
                AxisAlignedBB aabb = EntityPhantom.this.getEntityBoundingBox().grow(16.0, 64.0, 16.0);
                List<EntityPlayer> players = EntityPhantom.this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                if(!players.isEmpty()) {
                    players.sort((player1, player2) -> player1.posY > player2.posY ? -1 : 1);

                    for(EntityPlayer player : players) {
                        if(EntityAITarget.isSuitableTarget(EntityPhantom.this, player, false, false)) {
                            EntityPhantom.this.setAttackTarget(player);
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            EntityLivingBase target = EntityPhantom.this.getAttackTarget();
            if (target == null) {
                return false;
            }
            return EntityAITarget.isSuitableTarget(EntityPhantom.this, target, false, false);
        }
    }

    class AIPickAttack extends EntityAIBase {

        private int cooldown;

        private AIPickAttack() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase target = EntityPhantom.this.getAttackTarget();
            if (target == null) {
                return false;
            }
            return EntityAITarget.isSuitableTarget(EntityPhantom.this, target, false, false);
        }

        @Override
        public void startExecuting() {
            this.cooldown = 10;
            EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
            setAltitude();
        }

        @Override
        public void resetTask() {
            EntityPhantom.this.altitudePos = EntityPhantom.this.world.getHeight(EntityPhantom.this.altitudePos).up(10 + EntityPhantom.this.rand.nextInt(20));
        }

        @Override
        public void updateTask() {
            if(EntityPhantom.this.attackPhase == AttackPhase.CIRCLE) {
                --this.cooldown;
                if(this.cooldown <= 0) {
                    EntityPhantom.this.attackPhase = AttackPhase.SWOOP;
                    setAltitude();
                    this.cooldown = (8 + EntityPhantom.this.rand.nextInt(4)) * 20;
                    EntityPhantom.this.playSound(EFMSounds.ENTITY_PHANTOM_SWOOP, 10.0f, 0.95f + EntityPhantom.this.rand.nextFloat() * 0.1f);
                }
            }
        }

        private void setAltitude() {
            EntityPhantom.this.altitudePos = (new BlockPos(EntityPhantom.this.getAttackTarget())).up(20 + EntityPhantom.this.rand.nextInt(20));
            if(EntityPhantom.this.altitudePos.getY() < EntityPhantom.this.world.getSeaLevel()) {
                EntityPhantom.this.altitudePos = new BlockPos(EntityPhantom.this.altitudePos.getX(), EntityPhantom.this.world.getSeaLevel() + 1, EntityPhantom.this.altitudePos.getZ());
            }
        }
    }

    class AISweepAttack extends AIMove {

        private AISweepAttack() {
            super();
        }

        @Override
        public boolean shouldExecute() {
            return EntityPhantom.this.getAttackTarget() != null && EntityPhantom.this.attackPhase == AttackPhase.SWOOP;
        }

        @Override
        public boolean shouldContinueExecuting() {
            EntityLivingBase target = EntityPhantom.this.getAttackTarget();
            if (target == null) return false;
            if (!target.isEntityAlive()) return false;
            if (target instanceof EntityPlayer player && (player.isSpectator() || player.isCreative())) return false;
            if (!this.shouldExecute()) return false;

            if (EntityPhantom.this.ticksExisted % 20 == 0) {
                AxisAlignedBB aabb = EntityPhantom.this.getEntityBoundingBox().grow(16.0, 64.0, 16.0);
                List<EntityOcelot> cats = EntityPhantom.this.world.getEntitiesWithinAABB(EntityOcelot.class, aabb);
                Random rand = EntityPhantom.this.rand;
                if (!cats.isEmpty()) {
                    boolean shouldAttack = true;
                    for (EntityOcelot cat : cats) {
                        if (cat.isTamed()) {
                            shouldAttack = false;
                            cat.playSound(SoundEvents.ENTITY_CAT_PURREOW, 1.0f, cat.isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        }
                    }
                    return shouldAttack;
                }
            }
            return true;
        }

        @Override
        public void resetTask() {
            EntityPhantom.this.setAttackTarget(null);
            EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
        }

        @Override
        public void updateTask() {
            EntityLivingBase target = EntityPhantom.this.getAttackTarget();
            EntityPhantom.this.targetVec = new Vec3d(target.posX, target.posY + (double)target.height, target.posZ);
            if(EntityPhantom.this.getEntityBoundingBox().grow(0.2).intersects(target.getEntityBoundingBox())) {
                EntityPhantom.this.attackEntityAsMob(target);
                EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
                EntityPhantom.this.world.playSound(null, new BlockPos(EntityPhantom.this), EFMSounds.ENTITY_PHANTOM_ATTACK, SoundCategory.HOSTILE, 1.0f, 1.0f);
            } else if(EntityPhantom.this.collidedHorizontally || EntityPhantom.this.hurtTime > 0) {
                EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
            }
        }
    }

    class AIOrbitPoint extends AIMove {

        private float lookAngle;
        private float lookRadians; // range is 5-15
        private float pitchAngle; // range is -4 - 5
        private float clockwise;

        private AIOrbitPoint() { super(); }

        @Override
        public boolean shouldExecute() {
            return EntityPhantom.this.getAttackTarget() == null || EntityPhantom.this.attackPhase == AttackPhase.CIRCLE;
        }

        @Override
        public void startExecuting() {
            this.lookRadians = 5.0f + EntityPhantom.this.rand.nextFloat() * 10.0f;
            this.pitchAngle = -4.0f + EntityPhantom.this.rand.nextFloat() * 9.0f;
            this.clockwise = EntityPhantom.this.rand.nextBoolean() ? 1.0F : -1.0F;
            this.updateTargetVec();
        }

        @Override
        public void updateTask() {
            if(EntityPhantom.this.rand.nextInt(350) == 0) {
                this.pitchAngle = -4.0f + EntityPhantom.this.rand.nextFloat() * 9.0f;
            }

            if(EntityPhantom.this.rand.nextInt(250) == 0) {
                ++this.lookRadians;
                if(lookRadians > 15) {
                    lookRadians = 5;
                    clockwise = -clockwise;
                }
            }

            if(EntityPhantom.this.rand.nextInt(450) == 0) {
                this.lookAngle = EntityPhantom.this.rand.nextFloat() * 2.0f * (float)Math.PI;
                updateTargetVec();
            }

            if(closeToTarget()) {
                updateTargetVec();
            }

            if(EntityPhantom.this.targetVec.y < EntityPhantom.this.posY && !EntityPhantom.this.world.isAirBlock(new BlockPos(EntityPhantom.this).down(1))) {
                this.pitchAngle = Math.max(1.0f, this.pitchAngle);
                updateTargetVec();
            }

            if(EntityPhantom.this.targetVec.y > EntityPhantom.this.posY && !EntityPhantom.this.world.isAirBlock(new BlockPos(EntityPhantom.this).up(1))) {
                this.pitchAngle = Math.min(-1.0f, this.pitchAngle);
                updateTargetVec();
            }
        }

        private void updateTargetVec() {
            if(BlockPos.ORIGIN.equals(EntityPhantom.this.altitudePos)) {
                EntityPhantom.this.altitudePos = new BlockPos(EntityPhantom.this);
            }

            this.lookAngle += this.clockwise * 15.0f * Math.PI / 180.0f;
            EntityPhantom.this.targetVec = new Vec3d(EntityPhantom.this.altitudePos).add(this.lookRadians * MathHelper.cos(this.lookAngle), -4.0f + this.pitchAngle, this.lookRadians * MathHelper.sin(this.lookAngle));
        }
    }

    abstract class AIMove extends EntityAIBase {
        public AIMove() {this.setMutexBits(1);}

        protected boolean closeToTarget() {
            return EntityPhantom.this.targetVec.squareDistanceTo(EntityPhantom.this.posX, EntityPhantom.this.posY, EntityPhantom.this.posZ) < 4.0f;
        }
    }

    @Override
    protected EntityBodyHelper createBodyHelper() {
        return new BodyHelper(this);
    }

    class BodyHelper extends EntityBodyHelper {

        public BodyHelper(EntityLivingBase livingIn) {
            super(livingIn);
        }

        @Override
        public void updateRenderAngles() {
            EntityPhantom.this.rotationYawHead = EntityPhantom.this.renderYawOffset;
            EntityPhantom.this.renderYawOffset = EntityPhantom.this.rotationYaw;
        }
    }

    class MoveHelper extends EntityMoveHelper {

        private float motionVec = 0.1f;

        public MoveHelper(EntityLiving entitylivingIn) {
            super(entitylivingIn);
        }

        @Override
        public void onUpdateMoveHelper() {
            if(EntityPhantom.this.collidedHorizontally) {
                EntityPhantom.this.rotationYaw += 180.0f;
                this.motionVec = 0.1F;
            }

            float deltaX = (float) (EntityPhantom.this.targetVec.x - EntityPhantom.this.posX);
            float deltaY = (float) (EntityPhantom.this.targetVec.y - EntityPhantom.this.posY);
            float deltaZ = (float) (EntityPhantom.this.targetVec.z - EntityPhantom.this.posZ);
            double mag = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            double normalized = 1.0 - (double) Math.abs(deltaY * 0.7F) / mag;
            deltaX *= normalized;
            deltaZ *= normalized;

            mag = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            double trueMag = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ + deltaY * deltaY);
            float currentYaw = EntityPhantom.this.rotationYaw;
            float angle = (float) MathHelper.atan2(deltaZ, deltaX);
            float yaw = MathHelper.wrapDegrees(EntityPhantom.this.rotationYaw + 90.0F);
            float angleDeg = MathHelper.wrapDegrees((float) Math.toDegrees(angle));
            EntityPhantom.this.rotationYaw = approachDegrees(yaw, angleDeg, 4.0F) - 90.0F;
            EntityPhantom.this.renderYawOffset = EntityPhantom.this.rotationYaw;
            if(degreesDifferenceAbs(currentYaw, EntityPhantom.this.rotationYaw) < 3.0f) {
                this.motionVec = approach(this.motionVec, 1.8F, 0.005F * (1.8F / this.motionVec));
            } else {
                this.motionVec = approach(this.motionVec, 0.2F, 0.025F);
            }

            float newPitch = (float) -Math.toDegrees(MathHelper.atan2(-deltaY, mag));
            EntityPhantom.this.rotationPitch = newPitch;
            float yawOffset = EntityPhantom.this.rotationYaw + 90.0F;
            double velX = (double)(this.motionVec * MathHelper.cos((float) Math.toRadians(yawOffset))) * Math.abs((double) deltaX / trueMag);
            double velZ = (double)(this.motionVec * MathHelper.sin((float) Math.toRadians(yawOffset))) * Math.abs((double) deltaZ / trueMag);
            double velY = (double)(this.motionVec * MathHelper.sin((float) Math.toRadians(newPitch))) * Math.abs((double) deltaY / trueMag);
            EntityPhantom.this.motionX += (velX - EntityPhantom.this.motionX) * 0.2;
            EntityPhantom.this.motionY += (velY - EntityPhantom.this.motionY) * 0.2;
            EntityPhantom.this.motionZ += (velZ - EntityPhantom.this.motionZ) * 0.2;
        }
    }

    public static float approach(float angle, float min, float smoothingFactor) {
        smoothingFactor = MathHelper.abs(smoothingFactor);
        return angle < min ? MathHelper.clamp(angle + smoothingFactor, angle, min) : MathHelper.clamp(angle - smoothingFactor, min, angle);
    }

    public static float approachDegrees(float angle, float min, float max) {
        float diff = wrapSubtractDegrees(min, angle);
        return approach(angle, angle + diff, max);
    }

    public static float wrapSubtractDegrees(float a, float b) {
        float diff = MathHelper.wrapDegrees(a - b);
        return diff < 180.0F ? diff : diff - 360.0F;
    }

    public static float degreesDifferenceAbs(float a, float b) {
        float diff = MathHelper.wrapDegrees(a - b);
        return diff < 180.0F ? MathHelper.abs(diff) : MathHelper.abs(diff - 360.0F);
    }

    enum AttackPhase {
        CIRCLE, SWOOP
    }
}
