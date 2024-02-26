package com.serenibyss.etfuturum.items;

import com.serenibyss.etfuturum.entities.passive.fish.*;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFishBucket<T extends AbstractFish> extends ItemBucket implements IModelRegister {

    private static final String BUCKET_VARIANT_TAG = "BucketVariantTag";

    private final Class<T> fishType;

    public ItemFishBucket(Class<T> type, Block containedBlockIn) {
        super(containedBlockIn);
        this.fishType = type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        RayTraceResult result = this.rayTrace(worldIn, playerIn, false);

        //noinspection ConstantConditions, yes it can be null
        if (result == null || result.typeOfHit != Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        BlockPos pos = result.getBlockPos();
        EnumFacing side = result.sideHit;
        if (!worldIn.isBlockModifiable(playerIn, pos)) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) || side != EnumFacing.UP) {
            pos = pos.offset(side);
        }

        if (!playerIn.canPlayerEdit(pos, side, stack) || !tryPlaceContainedLiquid(playerIn, worldIn, pos)) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        try {
            this.placeFish(worldIn, stack, pos);
        } catch(InstantiationException | IllegalAccessException ignored) {}

        playerIn.addStat(StatList.getObjectUseStats(this));
        if (playerIn instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, pos, stack);
        }

        return !playerIn.capabilities.isCreativeMode
                ? new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET))
                : new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn) {
        if (this.containedBlock == Blocks.AIR) return false;
        IBlockState state = worldIn.getBlockState(posIn);
        Material material = state.getMaterial();
        boolean isSolid = !material.isSolid();
        boolean isReplaceable = state.getBlock().isReplaceable(worldIn, posIn);

        if (!worldIn.isAirBlock(posIn) && !isSolid && !isReplaceable) {
            return false;
        }

        if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER) {
            int x = posIn.getX();
            int y = posIn.getY();
            int z = posIn.getZ();
            worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

            for (int i = 0; i < 8; i++) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            if (!worldIn.isRemote && (isSolid || isReplaceable) && !material.isLiquid()) {
                worldIn.destroyBlock(posIn, true);
            }

            worldIn.playSound(player, posIn, EFMSounds.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
        }

        return true;
    }

    private void placeFish(World world, ItemStack stack, BlockPos pos) throws InstantiationException, IllegalAccessException {
        if (world.isRemote) return;

        AbstractFish fish;
        if (fishType == EntityCod.class) fish = new EntityCod(world);
        else if (fishType == EntitySalmon.class) fish = new EntitySalmon(world);
        else if (fishType == EntityPufferfish.class) fish = new EntityPufferfish(world);
        else if (fishType == EntityTropicalFish.class) fish = new EntityTropicalFish(world, stack);
        else return;

        fish.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        fish.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0f), 0.0f);
        if (stack.hasDisplayName()) fish.setCustomNameTag(stack.getDisplayName());
        fish.rotationYawHead = fish.rotationYaw;
        fish.renderYawOffset = fish.rotationYaw;

        IEntityLivingData data = null;
        if (fish instanceof EntityTropicalFish tropicalFish) {
            data = convertNBTToGroupData(tropicalFish, stack);
        }
        fish.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(fish)), data);
        fish.setFromBucket(true);
        world.spawnEntity(fish);
    }

    @Nullable
    private EntityTropicalFish.GroupData convertNBTToGroupData(EntityTropicalFish fish, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return null;

        int raw = tag.getInteger(BUCKET_VARIANT_TAG);
        int base = raw & 0xFF;
        int pattern = (raw & 0xFF00) >> 8;
        int bodyColor = (raw & 0xFF0000) >> 16;
        int patternColor = (raw & 0xFF000000) >> 24;
        return new EntityTropicalFish.GroupData(fish, base, pattern, bodyColor, patternColor);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (fishType == EntityTropicalFish.class) {
            NBTTagCompound bucketCompound = stack.getTagCompound();
            if (bucketCompound != null && bucketCompound.hasKey(BUCKET_VARIANT_TAG)) {
                int i = bucketCompound.getInteger(BUCKET_VARIANT_TAG);
                String bodyColor = "color.minecraft." + EntityTropicalFish.getBaseColor(i);
                String patternColor = "color.minecraft." + EntityTropicalFish.getPatternColor(i);

                for (int j = 0; j < EntityTropicalFish.SPECIAL_VARIANTS.length; ++j) {
                    if (i == EntityTropicalFish.SPECIAL_VARIANTS[j]) {
                        tooltip.add(I18n.format(EntityTropicalFish.getPredefinedName(j)));
                        return;
                    }
                }

                tooltip.add(I18n.format(EntityTropicalFish.getFishTypeName(i)));
                String coloring = I18n.format(bodyColor);
                if (!bodyColor.equals(patternColor)) {
                    coloring += ", " + I18n.format(patternColor);
                }

                tooltip.add(coloring);
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
