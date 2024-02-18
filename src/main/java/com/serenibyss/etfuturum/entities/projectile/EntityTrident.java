package com.serenibyss.etfuturum.entities.projectile;

import com.serenibyss.etfuturum.enchantment.EFMEnchantments;
import com.serenibyss.etfuturum.entities.weather.EFMEntityLightningBolt;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.mixin.trident.EntityArrowAccessor;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.EFMDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

public class EntityTrident extends EntityArrow {

    public static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private ItemStack thrownStack;
    private boolean dealtDamage;
    public int returningTicks;

    public EntityTrident(World worldIn) {
        super(worldIn);
        this.thrownStack = EFMItems.TRIDENT.getItemStack();
    }

    public EntityTrident(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.thrownStack = EFMItems.TRIDENT.getItemStack();
    }

    public EntityTrident(World worldIn, EntityLivingBase shooter, ItemStack stack) {
        super(worldIn, shooter);
        this.thrownStack = stack.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte) EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.LOYALTY, stack));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOYALTY_LEVEL, (byte)0);
    }

    public Entity findEntity() {
        return this.shootingEntity != null && this.world instanceof WorldServer ? ((WorldServer)this.world).getEntityFromUuid(this.shootingEntity.getUniqueID()) : null;
    }

    public void setLoyalty(boolean loyal) {
        this.noClip = loyal;
        byte b0 = this.dataManager.get(((EntityArrowAccessor)this).getCRITICAL());
        if(loyal) {
            this.dataManager.set(((EntityArrowAccessor)this).getCRITICAL(), (byte) (b0 | 2));
        }
        else {
            this.dataManager.set(((EntityArrowAccessor)this).getCRITICAL(), (byte) (b0 & ~2));
        }
    }

    public boolean isNoPhysics() {
        if(!this.world.isRemote) {
            return this.noClip;
        }
        else {
            return ((this.dataManager.get(((EntityArrowAccessor)this).getCRITICAL()) & 2) != 0);
        }
    }

    @Override
    public void onUpdate() {
        if(this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity shootingEntity = this.shootingEntity;
        boolean noPhys = isNoPhysics();
        if((this.dealtDamage || noPhys) && shootingEntity != null) {
            int loyalLevel = (Byte)this.dataManager.get(LOYALTY_LEVEL);
            if(loyalLevel > 0 && this.shouldReturnToThrower()) {
                if(!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1f);
                }
                this.setDead();
            }
            else if(loyalLevel > 0) {
                setLoyalty(true);
                Vec3d tridentDelta = new Vec3d(shootingEntity.posX - this.posX, shootingEntity.posY + (double)shootingEntity.getEyeHeight() - this.posY, shootingEntity.posZ - this.posZ);
                this.posY += tridentDelta.y * 0.015 * loyalLevel;
                if(this.world.isRemote) {
                    this.lastTickPosY = this.posY;
                }

                double loyaltyScale = 0.05 * (double)loyalLevel;

                Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ).scale(0.95).add(tridentDelta.normalize().scale(loyaltyScale));

                this.motionX = motion.x;
                this.motionY = motion.y;
                this.motionZ = motion.z;
                if(this.returningTicks == 0) {
                    this.playSound(EFMSounds.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = new BlockPos(((EntityArrowAccessor)this).getXTile(), ((EntityArrowAccessor)this).getYTile(), ((EntityArrowAccessor)this).getZTile());
        //BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR && !noPhys) {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.inGround && !noPhys) {
            int j = block.getMetaFromState(iblockstate);

            if ((block != ((EntityArrowAccessor)this).getInTile() || j != ((EntityArrowAccessor)this).getInData()) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05D))) {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                ((EntityArrowAccessor)this).setTicksInGround(0);
                ((EntityArrowAccessor)this).setTicksInAir(0);
            }
            else {
                int loyalLevel = this.dataManager.get(LOYALTY_LEVEL);
                if(this.pickupStatus != PickupStatus.ALLOWED || loyalLevel <= 0) {
                    ((EntityArrowAccessor)this).setTicksInGround(((EntityArrowAccessor)this).getTicksInGround() + 1);
                    //++this.ticksInGround;

                    if (((EntityArrowAccessor)this).getTicksInGround() >= 1200) {
                        this.setDead();
                    }
                }
            }

            ++this.timeInGround;
        }
        else {
            this.timeInGround = 0;
            ((EntityArrowAccessor)this).setTicksInAir(((EntityArrowAccessor)this).getTicksInAir() + 1);
            //++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null)
            {
                vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }

            Entity entity = this.findEntityOnPath(vec3d1, vec3d);

            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }

            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer entityplayer) {
                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }

            if (raytraceresult != null && !noPhys && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.isAirBorne = true;
            }

            if (this.getIsCritical()) {
                for (int k = 0; k < 4; ++k) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)k / 4.0D, this.posY + this.motionY * (double)k / 4.0D, this.posZ + this.motionZ * (double)k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if(noPhys) {
                this.rotationYaw = (float) (MathHelper.atan2(-this.motionX, -this.motionZ) * (180D / Math.PI));
            } else {
                this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            }

            for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f1 = 0.99F;

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }
            }
            this.motionX *= f1;
            this.motionY *= f1;
            this.motionZ *= f1;

            if (!this.hasNoGravity() && !noPhys) {
                this.motionY -= 0.05000000074505806D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    private boolean shouldReturnToThrower() {
        if(this.shootingEntity != null && this.shootingEntity.isEntityAlive())
            return !(this.shootingEntity instanceof EntityPlayer player) || player.isSpectator();
        else
            return false;
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @Nullable
    @Override
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        return this.dealtDamage ? null : super.findEntityOnPath(start, end);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;
        if(entity != null) {
            float damage = 8.0F;
            if (entity instanceof EntityLivingBase Elb) {
                damage += EnchantmentHelper.getModifierForCreature(this.thrownStack, Elb.getCreatureAttribute());
            }

            Entity myEnt = findEntity();
            DamageSource source = EFMDamageSource.causeTridentDamage(this, (myEnt == null ? this : myEnt));
            this.dealtDamage = true;
            SoundEvent sound = EFMSounds.ITEM_TRIDENT_HIT;
            if (entity.attackEntityFrom(source, damage) && entity instanceof EntityLivingBase elb) {
                if (myEnt instanceof EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments(elb, myEnt);
                    EnchantmentHelper.applyArthropodEnchantments(elb, myEnt);
                }
                this.arrowHit(elb);
            }

            this.motionX *= -0.01;
            this.motionY *= -0.1;
            this.motionZ *= -0.01;
            float volume = 1.0f;
            if (this.world.isThundering() && EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.CHANNELING, this.thrownStack) > 0) {
                BlockPos strikePos = entity.getPosition();
                if (this.world.canSeeSky(strikePos)) {
                    EFMEntityLightningBolt bolt = new EFMEntityLightningBolt(this.world, (double) strikePos.getX() + 0.5, strikePos.getY(), (double) strikePos.getZ() + 0.5, false);
                    bolt.setCaster(myEnt instanceof EntityPlayerMP playerMP ? playerMP : null);
                    this.world.addWeatherEffect(bolt);
                    sound = EFMSounds.ITEM_TRIDENT_THUNDER;
                    volume = 5.0F;
                }
            }

            this.playSound(sound, volume, 1.0F);

        }
        super.onHit(raytraceResultIn);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (this.shootingEntity == null || this.shootingEntity.getUniqueID() == entityIn.getUniqueID()) {
            if (!this.world.isRemote && (this.inGround || this.isNoPhysics()) && this.arrowShake <= 0) {
                boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode || this.isNoPhysics() && this.shootingEntity.getUniqueID() == entityIn.getUniqueID();
                if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
                    flag = false;
                }

                if (flag) {
                    entityIn.onItemPickup(this, 1);
                    this.setDead();
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("Trident", TAG_COMPOUND)) {
            this.thrownStack = new ItemStack(compound.getCompoundTag("Trident"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.LOYALTY, this.thrownStack));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Trident", this.thrownStack.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        // redirect an arrow sound to a trident sound
        if (soundIn == SoundEvents.ENTITY_ARROW_HIT) {
            soundIn = EFMSounds.ITEM_TRIDENT_HIT_GROUND;
        }
        super.playSound(soundIn, volume, pitch);
    }
}
