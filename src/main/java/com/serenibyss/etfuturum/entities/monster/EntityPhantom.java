package com.serenibyss.etfuturum.entities.monster;

import com.serenibyss.etfuturum.sounds.EtFuturumSounds;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.List;

public class EntityPhantom extends EntityFlying implements IMob {

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
                this.world.playSound(this.posX, this.posY, this.posZ, EtFuturumSounds.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.rand.nextFloat() * 0.05f, 0.95F + this.rand.nextFloat() * 0.05f, false);
            }
        }

        if(!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            world.removeEntity(this);
        }
    }

    class AIAttackPlayer extends EntityAIBase {

        private int cooldownTimer;

        private AIAttackPlayer() {
            this.cooldownTimer = 20;
        }

        @Override
        public boolean shouldExecute() {
            if(this.cooldownTimer > 0) {
                -- this.cooldownTimer;
                return false;
            } else {
                this.cooldownTimer = 60;
                AxisAlignedBB aabb = EntityPhantom.this.getEntityBoundingBox().grow(16.0, 64.0, 16.0);
                List<EntityPlayer> players = EntityPhantom.this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                if(!players.isEmpty()) {
                    players.sort((player1, player2) -> {
                        return player1.posY > player2.posY ? -1 : 1;
                    });

                    for(EntityPlayer player : players) {
                        if(EntityAITarget.isSuitableTarget(EntityPhantom.this, player, false, false)) {
                            EntityPhantom.this.setAttackTarget(player);
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return EntityAITarget.isSuitableTarget(EntityPhantom.this, EntityPhantom.this.getAttackTarget(), false, false);
        }
    }

    class AIPickAttack extends EntityAIBase {

        private int cooldown;

        private AIPickAttack() {}

        @Override
        public boolean shouldExecute() {
            return EntityAITarget.isSuitableTarget(EntityPhantom.this, EntityPhantom.this.getAttackTarget(), false, false);
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
                    // TODO(ONION): play the swoop sound
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
            if (target == null) {
                return false;
            } else if(!target.isEntityAlive()) {
                return false;
            } else {
                return !(target instanceof EntityPlayer) || !((EntityPlayer)target).isSpectator() && !((EntityPlayer)target).isCreative() ? this.shouldExecute() : false;
            }
        }

        @Override
        public void startExecuting() {}

        @Override
        public void resetTask() {
            EntityPhantom.this.setAttackTarget((EntityLivingBase) null);
            EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
        }

        @Override
        public void updateTask() {
            EntityLivingBase target = EntityPhantom.this.getAttackTarget();
            EntityPhantom.this.targetVec = new Vec3d(target.posX, target.posY + (double)target.height, target.posZ);
            if(EntityPhantom.this.getEntityBoundingBox().grow(0.2).intersects(target.getEntityBoundingBox())) {
                EntityPhantom.this.attackEntityAsMob(target);
                EntityPhantom.this.attackPhase = AttackPhase.CIRCLE;
                EntityPhantom.this.world.playSound(null, new BlockPos(EntityPhantom.this), EtFuturumSounds.ENTITY_PHANTOM_ATTACK, SoundCategory.HOSTILE, 1.0f, 1.0f);
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
            return EntityPhantom.this.getAttackTarget() != null && EntityPhantom.this.attackPhase == AttackPhase.CIRCLE;
        }

        @Override
        public void startExecuting() {
            this.lookRadians = 5.0f + EntityPhantom.this.rand.nextFloat() * 10.0f;
            this.pitchAngle = -4.0f + EntityPhantom.this.rand.nextFloat() * 9.0f;
            this.clockwise = EntityPhantom.this.rand.nextBoolean() ? 1.0F : -1.0F;
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

            if(EntityPhantom.this.targetVec.y < EntityPhantom.this.posY && !EntityPhantom.this.world.isAirBlock((new BlockPos(EntityPhantom.this)).down(1))) {
                this.pitchAngle = Math.max(1.0f, this.pitchAngle);
                updateTargetVec();
            }

            if(EntityPhantom.this.targetVec.y > EntityPhantom.this.posY && !EntityPhantom.this.world.isAirBlock((new BlockPos(EntityPhantom.this)).up(1))) {
                this.pitchAngle = Math.min(-1.0f, this.pitchAngle);
                updateTargetVec();
            }
        }

        private void updateTargetVec() {
            if(BlockPos.ORIGIN.equals(EntityPhantom.this.altitudePos)) {
                EntityPhantom.this.altitudePos = new BlockPos(EntityPhantom.this);
            }

            this.lookAngle += this.clockwise * 15.0f * Math.PI / 180.0f;
            EntityPhantom.this.targetVec = (new Vec3d(EntityPhantom.this.altitudePos)).add((double)(this.lookRadians * MathHelper.cos(this.lookAngle)), (double)(-4.0f + this.pitchAngle), (double)(this.lookRadians * MathHelper.sin(this.lookAngle)));
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

    class LookHelper extends EntityLookHelper {

        public LookHelper(EntityLiving entitylivingIn) {
            super(entitylivingIn);
        }

        @Override
        public void onUpdateLook() {}
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

        public void tick() {
            EntityPhantom phantom;
            if(EntityPhantom.this.collidedHorizontally) {
                phantom = EntityPhantom.this;
                phantom.rotationYaw += 180.0f;
                this.motionVec = 0.1F;
            }
        }
    }

    static enum AttackPhase {
        CIRCLE,
        SWOOP;

        private AttackPhase() {}
    }
}
