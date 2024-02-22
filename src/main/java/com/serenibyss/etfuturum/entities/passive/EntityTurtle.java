package com.serenibyss.etfuturum.entities.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.serenibyss.etfuturum.blocks.BlockTurtleEgg;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.load.enums.EFMEnumCreatureAttribute;
import com.serenibyss.etfuturum.pathfinding.WalkAndSwimNodeProcessor;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.MathUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class EntityTurtle extends EntityAnimal {

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> TRAVEL_POS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> GOING_HOME = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TRAVELLING = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
    private int isDigging;

    @SuppressWarnings("all") // MC api requires the Guava Predicate class here
    public static final Predicate<EntityTurtle> TARGET_DRY_BABY = baby -> baby.isChild() && !baby.isInWater();

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
        this.tasks.addTask(0, new EntityTurtle.AIPanic(this, 1.2));
        this.tasks.addTask(1, new EntityTurtle.AIMate(this, 1.0));
        this.tasks.addTask(1, new EntityTurtle.AILayEgg(this, 1.2));
        this.tasks.addTask(2, new EntityTurtle.AIPlayerTempt(this, 1.2, Items.WHEAT_SEEDS)); // todo change to seagrass
        this.tasks.addTask(3, new EntityTurtle.AIGoToWater(this, 1.2));
        this.tasks.addTask(4, new EntityTurtle.AIGoHome(this, 1.0));
        this.tasks.addTask(7, new EntityTurtle.AITravel(this, 1.0));
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
        return Objects.requireNonNull(EFMEnumCreatureAttribute.WATER);
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
        if (isEntityAlive() && getDigging() && this.isDigging >= 1 && this.isDigging % 5 == 0) {
            BlockPos pos = new BlockPos(this);
            if (this.world.getBlockState(pos.down()).getBlock() == Blocks.SAND) {
                this.world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(Blocks.SAND.getDefaultState()));
            }
        }
    }

    @Override
    protected void onGrowingAdult() {
        super.onGrowingAdult();
        if (!this.isChild() && this.world.getGameRules().getBoolean("doMobLoot")) {
            this.entityDropItem(EFMItems.SCUTE.getItemStack(), 1.0f);
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.1f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
            if (this.getAttackTarget() == null && (!this.isGoingHome() || !(this.getDistanceSq(this.getHome()) < 400.0))) {
                this.motionY -= 0.005;
            }
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightning) {
        this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
    }

    // todo
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (cause == DamageSource.LIGHTNING_BOLT) {
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
            if (this.turtle.isChild()) return false;
            if (this.turtle.hasEgg()) return true;
            if (this.turtle.getRNG().nextInt(700) != 0) return false;
            return this.turtle.getDistanceSq(this.turtle.getHome()) >= 4096.0D;
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
            BlockPos pos = this.turtle.getHome();
            boolean flag = this.turtle.getDistanceSq(pos) <= 256.0;
            if (flag) {
                this.wanderTime++;
            }

            if (this.turtle.getNavigator().noPath()) {
                Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 16, 3, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                if (vec == null) {
                    vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 8, 7, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                }

                if (vec != null && !flag && this.turtle.world.getBlockState(new BlockPos(vec)).getBlock() != Blocks.WATER) {
                    vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 16, 5, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                }

                if (vec == null) {
                    this.closeToHome = true;
                    return;
                }

                this.turtle.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, this.speed);
            }
        }
    }

    static class AIGoToWater extends EntityAIMoveToBlock {

        private final EntityTurtle turtle;

        public AIGoToWater(EntityTurtle creature, double speedIn) {
            super(creature, creature.isChild() ? 2.0 : speedIn, 24);
            this.turtle = creature;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !turtle.isInWater() && timeoutCounter <= 1200 && this.shouldMoveTo(turtle.world, destinationBlock);
        }

        // todo shouldMove() method substitute

        @Override
        public boolean shouldExecute() {
            if (turtle.isChild() && !turtle.isInWater()) return super.shouldExecute();
            return !turtle.isGoingHome() && !turtle.isInWater() && !turtle.hasEgg() && super.shouldExecute();
        }

        @Override
        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            Block block = worldIn.getBlockState(pos).getBlock();
            return block == Blocks.WATER;
        }
    }

    static class AILayEgg extends EntityAIMoveToBlock {

        private final EntityTurtle turtle;

        public AILayEgg(EntityTurtle creature, double speedIn) {
            super(creature, speedIn, 16);
            this.turtle = creature;
        }

        @Override
        public boolean shouldExecute() {
            return this.turtle.hasEgg() && this.turtle.getDistanceSq(this.turtle.getHome()) < 81.0D && super.shouldExecute();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.turtle.hasEgg() && this.turtle.getDistanceSq(this.turtle.getHome()) < 81.0D;
        }

        @Override
        public void updateTask() {
            super.updateTask();
            BlockPos pos = new BlockPos(turtle);
            if (!turtle.isInWater() && getIsAboveDestination()) {
                if( turtle.isDigging < 1) {
                    turtle.setDigging(true);
                } else if(turtle.isDigging > 200) {
                    World world = turtle.world;
                    world.playSound(null, pos, EFMSounds.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3f, 0.9f + world.rand.nextFloat() * 0.2f);
                    world.setBlockState(destinationBlock.up(), EFMBlocks.TURTLE_EGG.getBlock().getDefaultState().withProperty(BlockTurtleEgg.EGGS, turtle.rand.nextInt(4) + 1), 3);
                    turtle.setHasEgg(false);
                    turtle.setDigging(false);
                    turtle.setInLove(null);
                }

                if (turtle.getDigging()) {
                    turtle.isDigging++;
                }
            }
        }

        @Override
        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            if (!worldIn.isAirBlock(pos.up())) return false;
            Block block = worldIn.getBlockState(pos).getBlock();
            return block == Blocks.SAND;
        }
    }

    static class AIMate extends EntityAIMate {

        private final EntityTurtle turtle;

        public AIMate(EntityTurtle animal, double speedIn) {
            super(animal, speedIn);
            this.turtle = animal;
        }

        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && !turtle.hasEgg();
        }

        public void spawnBaby() {
            EntityPlayerMP player = animal.getLoveCause();
            if (player == null && targetMate.getLoveCause() != null) {
                player = targetMate.getLoveCause();
            }

            if (player != null) {
                player.addStat(StatList.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(player, animal, targetMate, null);
            }

            this.turtle.setHasEgg(true);
            animal.resetInLove();
            targetMate.resetInLove();
            if (turtle.world.getGameRules().getBoolean("doMobLoot")) {
                turtle.world.spawnEntity(new EntityXPOrb(turtle.world, animal.posX, animal.posY, animal.posZ, animal.getRNG().nextInt(7) + 1));
            }
        }
    }

    static class AIPanic extends EntityAIPanic {

        public AIPanic(EntityCreature creature, double speedIn) {
            super(creature, speedIn);
        }

        @Override
        public boolean shouldExecute() {
            if (creature.getRevengeTarget() == null && !creature.isBurning()) return false;
            BlockPos pos = this.getRandPos(creature.world, creature, 7, 4);
            if (pos != null) {
                randPosX = pos.getX();
                randPosY = pos.getY();
                randPosZ = pos.getZ();
                return true;
            }
            return this.findRandomPosition();
        }
    }

    static class AIPlayerTempt extends EntityAIBase {

        private final EntityTurtle turtle;
        private final double speed;
        private EntityPlayer player;
        private int tempingTime;
        private final Set<Item> attractItem;

        AIPlayerTempt(EntityTurtle turtle, double speed, Item attract) {
            this.turtle = turtle;
            this.speed = speed;
            this.attractItem = Sets.newHashSet(attract);
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            if (tempingTime > 0) {
                --tempingTime;
                return false;
            }

            player = turtle.world.getClosestPlayerToEntity(turtle, 10.0);
            if (player == null) {
                return false;
            }
            return attractItem.contains(player.getHeldItemMainhand().getItem()) || attractItem.contains(player.getHeldItemOffhand().getItem());
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.shouldExecute();
        }

        @Override
        public void resetTask() {
            player = null;
            turtle.getNavigator().clearPath();
            tempingTime = 100;
        }

        @Override
        public void updateTask() {
            turtle.getLookHelper().setLookPositionWithEntity(player, (float)(turtle.getHorizontalFaceSpeed() + 20), (float) turtle.getVerticalFaceSpeed());
            if (turtle.getDistanceSq(player) < 6.25) {
                turtle.getNavigator().clearPath();
            } else {
                turtle.getNavigator().tryMoveToEntityLiving(player, speed);
            }
        }
    }

    static class AITravel extends EntityAIBase {

        private final EntityTurtle turtle;
        private final double speed;
        private boolean interest;

        AITravel(EntityTurtle turtle, double speed) {
            this.turtle = turtle;
            this.speed = speed;
        }

        @Override
        public boolean shouldExecute() {
            return !turtle.isGoingHome() && !turtle.hasEgg() && turtle.isInWater();
        }

        @Override
        public void startExecuting() {
            Random random = this.turtle.rand;
            int x = random.nextInt(1025) - 512;
            int y = random.nextInt(9) - 4;
            int z = random.nextInt(1025) - 512;
            if ((double) y + turtle.posY > (double) (turtle.world.getSeaLevel() - 1)) {
                y = 0;
            }

            BlockPos pos = new BlockPos((double) x + turtle.posX, (double) y + turtle.posY, (double) z + turtle.posZ);
            turtle.setTravelPos(pos);
            turtle.setTravelling(true);
            this.interest = false;
        }

        @Override
        public void updateTask() {
            if (turtle.getNavigator().noPath()) {
                BlockPos pos = turtle.getTravelPos();
                Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 16, 3, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                if (vec == null) {
                    vec = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 8, 7, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                }

                if (vec != null) {
                    int i = MathHelper.floor(vec.x);
                    int j = MathHelper.floor(vec.z);

                    StructureBoundingBox mutableBoundingBox = new StructureBoundingBox(i - 34, 0, j - 34, i + 34, 0, j + 34);
                    if(!turtle.world.isAreaLoaded(mutableBoundingBox)) {
                        vec = null;
                    }
                }

                if (vec == null) {
                    interest = true;
                    return;
                }

                turtle.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, this.speed);
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return turtle.getNavigator().noPath() && !interest && turtle.isGoingHome() && turtle.isInLove() && !turtle.hasEgg();
        }

        @Override
        public void resetTask() {
            turtle.setTravelling(false);
            super.resetTask();
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
            return !this.entity.isInWater() && !this.turtle.isGoingHome() && !this.turtle.hasEgg() && super.shouldExecute();
        }
    }

    static class MoveHelper extends EntityMoveHelper {
        private final EntityTurtle turtle;

        MoveHelper(EntityTurtle turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        private void updateSpeed() {
            if (this.turtle.isInWater()) {
                this.turtle.motionY += 0.005;
                if (this.turtle.getDistanceSq(this.turtle.getHome()) >= 256) {
                    this.turtle.setAIMoveSpeed(Math.max(this.turtle.getAIMoveSpeed() / 2.0F, 0.08F));
                }

                if (this.turtle.isChild()) {
                    this.turtle.setAIMoveSpeed(Math.max(this.turtle.getAIMoveSpeed() / 3.0F, 0.06F));
                }
            } else if (this.turtle.onGround) {
                this.turtle.setAIMoveSpeed(Math.max(this.turtle.getAIMoveSpeed() / 2.0F, 0.06F));
            }
        }

        @Override
        public void onUpdateMoveHelper() {
            this.updateSpeed();
            if (this.action == Action.MOVE_TO && !this.turtle.getNavigator().noPath()) {
                double d0 = this.posX - this.turtle.posX;
                double d1 = this.posY - this.turtle.posY;
                double d2 = this.posZ - this.turtle.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.turtle.rotationYaw = this.limitAngle(this.turtle.rotationYaw, f, 90.0F);
                this.turtle.renderYawOffset = this.turtle.rotationYaw;
                float f1 = (float) (this.speed * this.turtle.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.turtle.setAIMoveSpeed(MathUtils.lerp(0.125F, this.turtle.getAIMoveSpeed(), f1));
                this.turtle.motionY = this.turtle.getAIMoveSpeed() * d1 * 0.1D;
            } else {
                this.turtle.setAIMoveSpeed(0);
            }
        }
    }

    static class PathNavigator extends PathNavigateSwimmer {

        public PathNavigator(EntityLiving entity, World worldIn) {
            super(entity, worldIn);
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
            if (this.entity instanceof EntityTurtle turtle && turtle.isTravelling()) {
                return this.world.getBlockState(pos).getBlock() == Blocks.WATER;
            }
            return this.world.getBlockState(pos.down()).getBlock() != Blocks.AIR;
        }
    }
}
