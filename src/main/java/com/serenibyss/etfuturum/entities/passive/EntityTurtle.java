package com.serenibyss.etfuturum.entities.passive;

import com.serenibyss.etfuturum.pathfinding.WalkAndSwimNodeProcessor;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

//@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.api.block.IFluidloggable", modid = ModIDs.FLUIDLOGGED)
public class EntityTurtle extends EntityAnimal { //implements IFluidloggable{

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> TRAVEL_POS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> GOING_HOME = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TRAVELLING = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private int isDigging;
    private static final Predicate<Entity> TARGET_DRY_BABY = (baby) -> {
        if(baby instanceof EntityLivingBase elb) {
            return elb.isChild() && baby.isInWater();
        }
        return false;
    };

    public EntityTurtle(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 0.4F);
        this.moveHelper = new MoveHelper(this);
        this.spawnableBlock = Blocks.SAND;
        this.stepHeight = 1.0F;
    }

    public void setHome(BlockPos pos) {
        this.dataManager.set(HOME_POS, pos);
    }

    private BlockPos getHome() {
        return this.dataManager.get(HOME_POS);
    }

    private void setTravelPos(BlockPos pos) {
        this.dataManager.set(TRAVEL_POS, pos);
    }

    private BlockPos getTravelPos() {
        return this.dataManager.get(TRAVEL_POS);
    }

    public boolean hasEgg() {
        return this.dataManager.get(HAS_EGG);
    }

    private void setHasEgg(boolean pregnant) {
        this.dataManager.set(HAS_EGG, pregnant);
    }

    public boolean getDigging() {
        return this.dataManager.get(IS_DIGGING);
    }

    private void setDigging(boolean digging) {
        this.isDigging = digging ? 1 : 0;
        this.dataManager.set(IS_DIGGING, digging);
    }

    private boolean isGoingHome() {
        return this.dataManager.get(GOING_HOME);
    }

    private void setGoingHome(boolean goHome) {
        this.dataManager.set(GOING_HOME, goHome);
    }

    private boolean isTravelling() {
        return this.dataManager.get(TRAVELLING);
    }

    private void setTravelling(boolean goTravel) {
        this.dataManager.set(TRAVELLING, goTravel);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HOME_POS, BlockPos.ORIGIN);
        this.dataManager.register(HAS_EGG, false);
        this.dataManager.register(TRAVEL_POS, BlockPos.ORIGIN);
        this.dataManager.register(GOING_HOME, false);
        this.dataManager.register(TRAVELLING, false);
        this.dataManager.register(IS_DIGGING, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("HomePosX", this.getHome().getX());
        compound.setInteger("HomePosY", this.getHome().getY());
        compound.setInteger("HomePosZ", this.getHome().getZ());
        compound.setBoolean("HasEgg", this.hasEgg());
        compound.setInteger("TravelPosX", this.getTravelPos().getX());
        compound.setInteger("TravelPosY", this.getTravelPos().getY());
        compound.setInteger("TravelPosZ", this.getTravelPos().getZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        int x = compound.getInteger("HomePosX");
        int y = compound.getInteger("HomePosY");
        int z = compound.getInteger("HomePosZ");
        this.setHome(new BlockPos(x,y,z));
        super.readEntityFromNBT(compound);
        this.setHasEgg(compound.getBoolean("HasEgg"));
        int x1 = compound.getInteger("TravelPosX");
        int y1 = compound.getInteger("TravelPosY");
        int z1 = compound.getInteger("TravelPosZ");
        this.setTravelPos(new BlockPos(x1, y1, z1));
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setHome(new BlockPos(this.posX, this.posY, this.posZ));
        this.setTravelPos(BlockPos.ORIGIN);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return pos.getY() < this.world.getSeaLevel() + 4 && super.getCanSpawnHere();
    }

    @Override
    protected void initEntityAI() {
        //this.tasks.addTask(0, new EntityTurtle.AIPanic(this, 1.2));
        //this.tasks.addTask(1, new EntityTurtle.AIMate(this, 1.0));
        //this.tasks.addTask(1, new EntityTurtle.AILayEgg(this, 1.2));
        //this.tasks.addTask(2, new EntityTurtle.AIPlayerTempt(this, 1.2));
        //this.tasks.addTask(3, new EntityTurtle.AIGoToWater(this, 1.2));
        this.tasks.addTask(4, new EntityTurtle.AIGoHome(this, 1.0));
        //this.tasks.addTask(7, new EntityTurtle.AITravel(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityTurtle.AIWander(this, 1.0, 100));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED; // todo(onion) change to EFM#WATER
    }

    @Override
    public int getTalkInterval() {
        return 200;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return !this.isInWater() && this.onGround && !this.isChild() ? EFMSounds.ENTITY_TURTLE_AMBIENT_LAND : super.getAmbientSound();
    }

    protected void playSwimSound(float volume) {
        this.playSound(this.getSwimSound(), volume, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return EFMSounds.ENTITY_TURTLE_SWIM;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isChild() ? EFMSounds.ENTITY_TURTLE_HURT_BABY : EFMSounds.ENTITY_TURTLE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isChild() ? EFMSounds.ENTITY_TURTLE_DEATH_BABY : EFMSounds.ENTITY_TURTLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        SoundEvent event = this.isChild() ? EFMSounds.ENTITY_TURTLE_SHAMBLE_BABY : EFMSounds.ENTITY_TURTLE_SHAMBLE;
        this.playSound(event, 0.15F, 1.0F);
    }

    @Override
    public void setScaleForAge(boolean child) {
        this.setScale(child ? 0.3f : 1.0f);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigator(this, worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityTurtle(this.world);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return !this.isGoingHome() && world.getBlockState(pos).getBlock() == Blocks.WATER ? 10.0F : super.getBlockPathWeight(pos);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getDigging() && this.isDigging >= 1 && this.isDigging % 5 == 0) {
            BlockPos blockpos = new BlockPos(this);
            if(this.world.getBlockState(blockpos.down()).getBlock() == Blocks.SAND) {
                this.world.playEvent(2001, blockpos, Block.getStateId(Blocks.SAND.getDefaultState()));
            }
        }
    }

    @Override
    protected void onGrowingAdult() {
        super.onGrowingAdult();
        if(this.world.getGameRules().getBoolean("doModLoot")) {
            // todo(onion) drop a scute
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(this.isServerWorld() && this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.1f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
            if(this.getAttackTarget() == null && (!this.isGoingHome() || !(this.getDistanceSq(this.getHome()) < 400.0))) {
                this.motionY -= 0.005;
            }
        }
        else {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if(cause == DamageSource.LIGHTNING_BOLT) {
            this.entityDropItem(new ItemStack(Items.BOWL, 1), 0.0F);
        }
    }

    static class AIGoHome extends EntityAIBase {
        private final EntityTurtle turtle;
        private final double speed;
        private boolean closeToHome;
        private int wanderTime;

        AIGoHome(EntityTurtle turtle, double speed) {
            this.turtle = turtle;
            this.speed = speed;
        }

        @Override
        public boolean shouldExecute() {
            if(this.turtle.isChild()) {
                return false;
            } else if(this.turtle.hasEgg()) {
                return true;
            } else if(this.turtle.getRNG().nextInt(700) != 0) {
                return false;
            } else {
                return this.turtle.getDistanceSq(this.turtle.getHome()) >= 4096.0D;
            }
        }

        @Override
        public void startExecuting() {
            this.turtle.setGoingHome(true);
            this.closeToHome = false;
            this.wanderTime = 0;
        }

        @Override
        public void resetTask() {
            this.turtle.setGoingHome(false);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.turtle.getDistanceSq(this.turtle.getHome()) >= 49.0 && !this.closeToHome && this.wanderTime <= 600;
        }

        @Override
        public void updateTask() {
            BlockPos blockPos = this.turtle.getHome();
            boolean flag = this.turtle.getDistanceSq(blockPos) <= 256.0;
            if(flag) {
                ++this.wanderTime;
            }

            if(this.turtle.getNavigator().noPath()) {
                Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 16, 3, new Vec3d((double)blockPos.getX(), (double)blockPos.getY(),(double)blockPos.getZ()));
                if(vec == null) {
                    vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 8, 7, new Vec3d((double)blockPos.getX(), (double)blockPos.getY(),(double)blockPos.getZ()));
                }

                if(vec != null && !flag && this.turtle.world.getBlockState(new BlockPos(vec)).getBlock() != Blocks.WATER) {
                    vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 16, 5, new Vec3d((double)blockPos.getX(), (double)blockPos.getY(),(double)blockPos.getZ()));
                }

                if(vec == null) {
                    this.closeToHome = true;
                    return;
                }

                this.turtle.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, this.speed);
            }
        }
    }

    static class AIGoToWater extends EntityAIMoveToBlock {

        public AIGoToWater(EntityCreature creature, double speedIn, int length) {
            super(creature, speedIn, length);
        }

        @Override
        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            return false;
        }
    }

    static class AILayEgg extends EntityAIMoveToBlock {

        public AILayEgg(EntityCreature creature, double speedIn, int length) {
            super(creature, speedIn, length);
        }

        @Override
        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            return false;
        }
    }

    static class AIMate extends EntityAIMate {

        public AIMate(EntityAnimal animal, double speedIn) {
            super(animal, speedIn);
        }
    }

    static class AIPanic extends EntityAIPanic {

        public AIPanic(EntityCreature creature, double speedIn) {
            super(creature, speedIn);
        }
    }

    static class AIPlayerTempt extends EntityAIBase {

        @Override
        public boolean shouldExecute() {
            return false;
        }
    }

    static class AITravel extends EntityAIBase {

        @Override
        public boolean shouldExecute() {
            return false;
        }
    }

    static class AIWander extends EntityAIWander {
        private final EntityTurtle turtle;
        public AIWander(EntityTurtle turtle, double speedIn, int chance) {
            super(turtle, speedIn, chance);
            this.turtle = turtle;
        }

        @Override
        public boolean shouldExecute() {
            return !this.entity.isInWater() && !this.turtle.isGoingHome() && !this.turtle.hasEgg() ? super.shouldExecute() : false;
        }
    }

    static class MoveHelper extends EntityMoveHelper {
        private final EntityTurtle turtle;

        MoveHelper(EntityTurtle turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        private void updateSpeed() {
            if(this.turtle.isInWater()) {
                this.turtle.motionY += 0.005;
            }
        }
    }

    static class PathNavigator extends PathNavigateSwimmer {

        public PathNavigator(EntityLiving entitylivingIn, World worldIn) {
            super(entitylivingIn, worldIn);
        }

        @Override
        protected boolean canNavigate() {
            return true;
        }

        @Override
        protected PathFinder getPathFinder() {
            return new PathFinder(new WalkAndSwimNodeProcessor());
        }

        @Override
        public boolean canEntityStandOnPos(BlockPos pos) {
            if(this.entity instanceof EntityTurtle) {
                EntityTurtle entityTurtle = (EntityTurtle)this.entity;
                if(entityTurtle.isTravelling())
                    return this.world.getBlockState(pos).getBlock() == Blocks.WATER;
            }

            return !(this.world.getBlockState(pos.down()).getBlock() == Blocks.AIR);
        }
    }
}
