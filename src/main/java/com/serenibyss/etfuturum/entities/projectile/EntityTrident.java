package com.serenibyss.etfuturum.entities.projectile;

import com.serenibyss.etfuturum.enchantment.EFMEnchantments;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.EFMDamageSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

public class EntityTrident extends EntityArrow {

    public static final DataParameter<Byte> LOYALTY_LEVEL;
    private ItemStack thrownStack;
    private boolean dealtDamage;
    public int returningTicks;

    public EntityTrident(World worldIn) {
        super(worldIn);
        this.thrownStack = new ItemStack(EFMItems.TRIDENT.getItem());
    }

    public EntityTrident(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.thrownStack = new ItemStack(EFMItems.TRIDENT.getItem());
    }

    public EntityTrident(World worldIn, EntityLivingBase shooter, ItemStack stack) {
        super(worldIn, shooter);
        this.thrownStack = new ItemStack(EFMItems.TRIDENT.getItem());
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
        byte b0 = this.dataManager.get(CRITICAL);
        if(loyal)
            this.dataManager.set(CRITICAL, (byte)(b0 | 2));
        else
            this.dataManager.set(CRITICAL, (byte)(b0 & ~2));
    }

    public boolean getLoyalty() {
        if(!this.world.isRemote) {
            return this.noClip;
        }
        else {
            return ((this.dataManager.get(CRITICAL) & 2) != 0);
        }
    }

    @Override
    public void onUpdate() {
        if(this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.shootingEntity;
        if((this.dealtDamage || getLoyalty()) && entity != null) {
            int loyalLevel = (Byte)this.dataManager.get(LOYALTY_LEVEL);
            if(loyalLevel > 0 && this.shouldReturnToThrower()) {
                if(!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1f);
                }
                this.setDead();
            }
            else if(loyalLevel > 0) {
                setLoyalty(true);
                Vec3d tridentPos = new Vec3d(entity.posX - this.posX, entity.posY + (double)entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
                this.posY += tridentPos.y * 0.015 * loyalLevel;;
                if(this.world.isRemote)
                    this.lastTickPosY = this.posY;

                tridentPos = tridentPos.normalize();
                double loyalPerTick = 0.05 * (double)loyalLevel;
                this.motionX += tridentPos.x * loyalPerTick - this.motionX * 0.05;
                this.motionY += tridentPos.y * loyalPerTick - this.motionY * 0.05;
                this.motionZ += tridentPos.z * loyalPerTick - this.motionZ * 0.05;
                if(this.returningTicks == 0) {
                    this.playSound(EFMSounds.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.onUpdate();
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
        float damage = 8.0F;
        if(entity instanceof EntityLivingBase Elb)
            damage += EnchantmentHelper.getModifierForCreature(this.thrownStack, Elb.getCreatureAttribute());

        Entity myEnt = findEntity();
        DamageSource source = EFMDamageSource.causeTridentDamage(this, (Entity)(myEnt == null ? this : myEnt));
        this.dealtDamage = true;
        SoundEvent sound = EFMSounds.ITEM_TRIDENT_HIT;
        if(entity.attackEntityFrom(source, damage) && entity instanceof EntityLivingBase elb) {
            if(myEnt instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(elb, myEnt);
                EnchantmentHelper.applyArthropodEnchantments(elb, myEnt);
            }
            this.arrowHit(elb);
        }

        this.motionX *= -0.01;
        this.motionY *= -0.1;
        this.motionZ *= -0.01;
        float volume = 1.0f;
        if(this.world.isThundering() && EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.CHANNELING, this.thrownStack) > 0) {
            BlockPos strikePos = entity.getPosition();
            if(this.world.canSeeSky(strikePos)) {
                EntityLightningBolt bolt = new EntityLightningBolt(this.world, (double)strikePos.getX() + 0.5, (double)strikePos.getY(), (double)strikePos.getZ() + 0.5, false);
                // todo(onion): maybe make this an advancement trigger?
                sound = EFMSounds.ITEM_TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }

        this.playSound(sound, volume, 1.0F);
        super.onHit(raytraceResultIn);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if(this.shootingEntity == null || this.shootingEntity.getUniqueID() == entityIn.getUniqueID())
            super.onCollideWithPlayer(entityIn);
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

    static {
        LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    }
}
