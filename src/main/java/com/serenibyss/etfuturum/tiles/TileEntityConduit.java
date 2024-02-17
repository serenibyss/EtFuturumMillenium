package com.serenibyss.etfuturum.tiles;

import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.client.particle.ParticleNautilus;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.ModIDs;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityConduit extends TileEntity implements ITickable {

    public int ticksExisted;
    private float activeRotation;
    private boolean active;
    private boolean eyeOpen;
    private final List<BlockPos> prismarinePositions = new ArrayList<>();
    @Nullable
    private EntityLiving target;
    @Nullable
    private UUID targetUuid;
    private long nextSoundTime;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("target_uuid")) {
            this.targetUuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("target_uuid"));
        } else {
            this.targetUuid = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.target != null) {
            compound.setTag("target_uuid", NBTUtil.createUUIDTag(this.target.getUniqueID()));
        }
        return compound;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // todo tile entity type?
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void update() {
        this.ticksExisted++;
        long i = this.world.getTotalWorldTime();
        if (i % 40 == 0) {
            this.setActive(this.shouldBeActive());
            if (!world.isRemote && isActive()) {
                this.addEffectsToPlayers();
                this.attackMobs();
            }
        }

        if (i % 80 == 0 && isActive()) {
            this.playSound(EFMSounds.BLOCK_CONDUIT_AMBIENT);
        }

        if (i > this.nextSoundTime && isActive()) {
            this.nextSoundTime = i + 60 + this.world.rand.nextInt(40);
            this.playSound(EFMSounds.BLOCK_CONDUIT_AMBIENT_SHORT);
        }

        if (world.isRemote) {
            this.updateClientTarget();
            this.spawnParticles();
            if (this.isActive()) {
                this.activeRotation++;
            }
        }
    }

    private boolean shouldBeActive() {
        this.prismarinePositions.clear();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = this.pos.add(x, y, z);
                    if (!doesBlockHaveWater(world, pos)) {
                        return false;
                    }
                }
            }
        }

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    int i2 = Math.abs(x);
                    int l = Math.abs(y);
                    int i1 = Math.abs(z);
                    if (x == 0 && (l == 2 || i1 == 2) || y == 0 && (i2 == 2 || i1 == 2) || z == 0 && (i2 == 2 || l == 2)) {
                        BlockPos pos = this.pos.add(x, y, z);
                        IBlockState state = this.world.getBlockState(pos);
                        if (state.getBlock() == Blocks.PRISMARINE || state.getBlock() == Blocks.SEA_LANTERN) {
                            this.prismarinePositions.add(pos);
                        }
                    }
                }
            }
        }

        this.setEyeOpen(this.prismarinePositions.size() >= 42);
        return this.prismarinePositions.size() >= 16;
    }

    private static boolean doesBlockHaveWater(World world, BlockPos pos) {
        if (Loader.isModLoaded(ModIDs.FLUIDLOGGED)) {
            FluidState state = FluidloggedUtils.getFluidState(world, pos);
            return state.getFluid() == FluidRegistry.WATER;
        } else {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == EFMBlocks.CONDUIT.getBlock()) {
                // conduit must always succeed
                return true;
            } else if (state.getBlock() instanceof IFluidBlock fluid) {
                return fluid.getFluid() == FluidRegistry.WATER;
            }
            return state.getMaterial() == Material.WATER;
        }
    }

    private void addEffectsToPlayers() {
        int i = this.prismarinePositions.size();
        int j = i / 7 * 16;
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).grow(j).expand(0, world.getHeight(), 0);
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
        if (!players.isEmpty()) {
            for (EntityPlayer player : players) {
                if (this.pos.distanceSq(player.posX, player.posY, player.posZ) < j * j && player.isWet()) {
                    // maybe one day, we'll make this its own standalone effect
                    player.addPotionEffect(new PotionEffect(Objects.requireNonNull(MobEffects.HASTE), 260, 0, true, true));
                    player.addPotionEffect(new PotionEffect(Objects.requireNonNull(MobEffects.WATER_BREATHING), 260, 0, true, true));
                    player.addPotionEffect(new PotionEffect(Objects.requireNonNull(MobEffects.NIGHT_VISION), 260, 0, true, true));
                    //player.addPotionEffect(new PotionEffect(EFMEffects.CONDUIT_POWER, 260, 0, true, true));
                }
            }
        }
    }

    private void attackMobs() {
        EntityLiving entity = this.target;
        int i = this.prismarinePositions.size();
        if (i < 42) {
            this.target = null;
        } else if (this.target == null && this.targetUuid != null) {
            this.target = this.findExistingTarget();
            this.targetUuid = null;
        } else if (this.target == null) {
            List<EntityLiving> entities = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getAreaOfEffect(), e -> e instanceof IMob && e.isWet());
            if (!entities.isEmpty()) {
                this.target = entities.get(world.rand.nextInt(entities.size()));
            }
        } else if (!this.target.isEntityAlive() || pos.distanceSq(target.posX, target.posY, target.posZ) >= 64) {
            this.target = null;
        }

        if (this.target != null) {
            this.world.playSound(null, target.posX, target.posY, target.posZ, EFMSounds.BLOCK_CONDUIT_ATTACK_TARGET, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.target.attackEntityFrom(DamageSource.MAGIC, 4.0f);
        }

        if (entity != this.target) {
            IBlockState state = world.getBlockState(pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 2);
        }
    }

    private void updateClientTarget() {
        if (this.targetUuid == null) {
            this.target = null;
        } else if (this.target == null || !this.target.getUniqueID().equals(this.targetUuid)) {
            this.target = findExistingTarget();
            if (this.target == null) {
                this.targetUuid = null;
            }
        }
    }

    private AxisAlignedBB getAreaOfEffect() {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).grow(8);
    }

    @Nullable
    private EntityLiving findExistingTarget() {
        List<EntityLiving> entities = this.world.getEntitiesWithinAABB(EntityLiving.class, getAreaOfEffect(), e -> e.getUniqueID().equals(this.targetUuid));
        return entities.size() == 1 ? entities.get(0) : null;
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        Random random = this.world.rand;
        float f = MathHelper.sin((float) (this.ticksExisted + 35) * 0.1f) / 2.0f + 0.5f;
        f = (f * f + f) * 0.3f;
        Vec3d vec = new Vec3d((float)this.pos.getX() + 0.5F, (float)this.pos.getY() + 1.5F + f, (float)this.pos.getZ() + 0.5F);

        for (BlockPos pos : this.prismarinePositions) {
            if (random.nextInt(50) == 0) {
                float f1 = -0.5F + random.nextFloat();
                float f2 = -2.0F + random.nextFloat();
                float f3 = -0.5F + random.nextFloat();
                BlockPos blockpos1 = pos.subtract(this.pos);
                Vec3d vec3d1 = new Vec3d(f1, f2, f3).add(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleNautilus(this.world, vec.x, vec.y, vec.z, vec3d1.x, vec3d1.y, vec3d1.z));;
            }
        }

        if (this.target != null) {
            Vec3d vec3d2 = new Vec3d(this.target.posX, this.target.posY + (double)this.target.getEyeHeight(), this.target.posZ);
            float f4 = (-0.5F + random.nextFloat()) * (3.0F + this.target.width);
            float f5 = -1.0F + random.nextFloat() * this.target.height;
            float f6 = (-0.5F + random.nextFloat()) * (3.0F + this.target.width);
            Vec3d vec3d3 = new Vec3d(f4, f5, f6);
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleNautilus(this.world, vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z));
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @SideOnly(Side.CLIENT)
    public boolean isEyeOpen() {
        return this.eyeOpen;
    }

    private void setActive(boolean active) {
        if (active != this.active) {
            this.playSound(active ? EFMSounds.BLOCK_CONDUIT_ACTIVATE : EFMSounds.BLOCK_CONDUIT_DEACTIVATE);
        }
        this.active = active;
    }

    private void setEyeOpen(boolean eyeOpen) {
        this.eyeOpen = eyeOpen;
    }

    @SideOnly(Side.CLIENT)
    public float getActiveRotation(float partialTicks) {
        return (this.activeRotation + partialTicks) * -0.0375f;
    }

    public void playSound(SoundEvent sound) {
        this.world.playSound(null, this.pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
