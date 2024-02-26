package com.serenibyss.etfuturum.items;

import com.serenibyss.etfuturum.entities.passive.fish.*;
import com.serenibyss.etfuturum.mixin.fish.ItemBucketMixin;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ItemFishBucket<T extends AbstractFish> extends ItemBucket implements IModelRegister {
    private final Class<T> fishType;
    public ItemFishBucket(Class<T> type, Block containedBlockIn) {
        super(containedBlockIn);
        this.fishType = type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        boolean flag = false;
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;

        if (raytraceresult == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            else if (flag)
            {
                if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getMaterial();

                    if (material == Material.WATER && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                        playerIn.addStat(StatList.getObjectUseStats(this));
                        playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, Items.WATER_BUCKET));
                    }
                    else if (material == Material.LAVA && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                        playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                        playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, Items.LAVA_BUCKET));
                    }
                    else
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                    }
                }
            }
            else
            {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

                if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1))
                {
                    try {
                        this.placeFish(worldIn, itemstack, blockpos1);
                    } catch(InstantiationException | IllegalAccessException ie) {

                    }
                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack);
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET)) : new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
        }
    }

    public void placeFish(World world, ItemStack stack, BlockPos pos) throws InstantiationException, IllegalAccessException {
        if(!world.isRemote) {
            if(fishType == EntityCod.class) {
                var fish = new EntityCod(world);
                fish.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                fish.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                fish.setCustomNameTag(stack.getDisplayName());
                fish.rotationYawHead = fish.rotationYaw;
                fish.renderYawOffset = fish.rotationYaw;
                fish.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(fish)), null);
                fish.setFromBucket(true);
                world.spawnEntity(fish);
            }
            if(fishType == EntitySalmon.class) {
                var fish = new EntitySalmon(world);
                fish.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                fish.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                fish.setCustomNameTag(stack.getDisplayName());
                fish.rotationYawHead = fish.rotationYaw;
                fish.renderYawOffset = fish.rotationYaw;
                fish.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(fish)), null);
                fish.setFromBucket(true);
                world.spawnEntity(fish);
            }
            if(fishType == EntityPufferfish.class) {
                var fish = new EntityPufferfish(world);
                fish.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                fish.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                fish.setCustomNameTag(stack.getDisplayName());
                fish.rotationYawHead = fish.rotationYaw;
                fish.renderYawOffset = fish.rotationYaw;
                fish.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(fish)), null);
                fish.setFromBucket(true);
                world.spawnEntity(fish);
            }
            if(fishType == EntityTropicalFish.class) {
                var fish = new EntityTropicalFish(world, stack);
                fish.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                fish.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                fish.setCustomNameTag(stack.getDisplayName());
                fish.rotationYawHead = fish.rotationYaw;
                fish.renderYawOffset = fish.rotationYaw;
                fish.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(fish)), convertNBTToGroupData(fish, stack));
                fish.setFromBucket(true);
                world.spawnEntity(fish);
            }
        }
    }

    private EntityTropicalFish.GroupData convertNBTToGroupData(EntityTropicalFish fish, ItemStack stack) {
        if(stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            int i1 = compound.getInteger("BucketVariantTag");
            int i = i1 & 255;
            int j = (i1 & '\uff00') >> 8;
            int k = (i1 & 16711680) >> 16;
            int l = (i1 & -16777216) >> 24;
            return new EntityTropicalFish.GroupData(fish, i, j, k, l);
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
