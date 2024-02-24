package com.serenibyss.etfuturum.entities.passive.fish;

import com.serenibyss.etfuturum.entities.ai.EFMEntityAIWanderSwim;
import com.serenibyss.etfuturum.entities.base.EFMEntityWaterMob;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class AbstractFish extends EFMEntityWaterMob {
    private static final DataParameter<Boolean> FROM_BUCKET = EntityDataManager.createKey(AbstractFish.class, DataSerializers.BOOLEAN);
    public AbstractFish(World worldIn) {
        super(worldIn);
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.65f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0);
    }

    @Override
    public boolean isNoDespawnRequired() {
        return !isFromBucket() && super.isNoDespawnRequired();
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this);
        return this.world.getBlockState(pos).getBlock() == Blocks.WATER && this.world.getBlockState(pos.up()).getBlock() == Blocks.WATER ? super.getCanSpawnHere() : false;
    }

    @Override
    protected boolean canDespawn() {
        return !isFromBucket() && !hasCustomName();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FROM_BUCKET, false);
    }

    private boolean isFromBucket() {
        return this.dataManager.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean bucket) {
        dataManager.set(FROM_BUCKET, bucket);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 8.0f, 1.6, 1.4));
        this.tasks.addTask(4, new AbstractFish.AISwim(this));
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.01f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) 0.9F;
            this.motionY *= (double) 0.9F;
            this.motionZ *= (double) 0.9F;
            if (this.getAttackTarget() == null) {
                this.motionY -= 0.005D;
            }
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public void onEntityUpdate() {
        if(!isInWater() && this.onGround && collidedVertically) {
            this.motionY += 0.4;
            this.motionX += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.05);
            this.motionZ += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.05);
            this.onGround = false;
            this.isAirBorne = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
        }
        super.onEntityUpdate();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.getItem() == Items.WATER_BUCKET && isEntityAlive()) {
            this.playSound(EFMSounds.ITEM_BUCKET_FILL_FISH, 1.0f, 1.0f);
            stack.shrink(1);
            ItemStack stack1 = getFishBucket();
            this.setBucketData(stack1);
            if(!this.world.isRemote) {
                // criteria
            }

            if(stack.isEmpty()) {
                player.setHeldItem(hand, stack1);
            } else if(!player.inventory.addItemStackToInventory(stack1)) {
                player.dropItem(stack1, false);
            }

            this.setDead();
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    protected void setBucketData(ItemStack stack) {
        if(hasCustomName()) {
            stack.setStackDisplayName(getCustomNameTag());
        }
    }

    protected abstract ItemStack getFishBucket();
    protected boolean isAlive() {
        return true;
    }
    protected abstract SoundEvent getFlopSound();

    @Override
    protected SoundEvent getSwimSound() {
        return EFMSounds.ENTITY_FISH_SWIM;
    }

    static class AISwim extends EFMEntityAIWanderSwim {
        private final AbstractFish fish;

        public AISwim(AbstractFish creatureIn) {
            super(creatureIn, 1.0, 40);
            this.fish = creatureIn;
        }

        @Override
        public boolean shouldExecute() {
            return fish.isAlive() && super.shouldExecute();
        }
    }

    static class MoveHelper extends EntityMoveHelper {
        private final AbstractFish fish;
        public MoveHelper(AbstractFish entitylivingIn) {
            super(entitylivingIn);
            this.fish = entitylivingIn;
        }

        @Override
        public void onUpdateMoveHelper() {
            if(fish.isInsideOfMaterial(Material.WATER)) {
                this.fish.motionY += 0.005;
            }

            if(this.action == Action.MOVE_TO && !fish.getNavigator().noPath()) {
                double dx = posX - fish.posX;
                double dy = posY - fish.posY;
                double dz = posZ - fish.posZ;
                double del = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                dy /= del;
                float f = (float)(MathHelper.atan2(dz, dx) * (180f / (float)Math.PI)) - 90.0f;
                fish.rotationYaw = limitAngle(fish.rotationYaw, f, 90.0f);
                fish.renderYawOffset = fish.rotationYaw;
                float f1 = (float)(speed * fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                fish.setAIMoveSpeed(fish.getAIMoveSpeed() + (f1 - fish.getAIMoveSpeed()) * 0.125f);
                fish.motionY += (double) fish.getAIMoveSpeed() * dy * 0.1;
            } else {
                fish.setAIMoveSpeed(0.0f);
            }
        }
    }
}
