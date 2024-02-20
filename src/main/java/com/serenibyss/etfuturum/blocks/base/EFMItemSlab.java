package com.serenibyss.etfuturum.blocks.base;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EFMItemSlab extends ItemBlock {

    private final EFMBlockHalfSlab halfSlab;
    private final EFMBlockDoubleSlab doubleSlab;

    public EFMItemSlab(EFMBlockHalfSlab halfSlab, EFMBlockDoubleSlab doubleSlab) {
        super(halfSlab);
        this.halfSlab = halfSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return halfSlab.getTranslationKey();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            IBlockState state = worldIn.getBlockState(pos);

            if (state.getBlock() == this.halfSlab) {
                EnumBlockHalf half = state.getValue(EFMBlockSlab.HALF);

                if ((facing == EnumFacing.UP && half == EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && half == EnumBlockHalf.TOP)) {
                    IBlockState doubleState = doubleSlab.getDefaultState();
                    AxisAlignedBB aabb = doubleState.getCollisionBoundingBox(worldIn, pos);

                    if (aabb != Block.NULL_AABB && worldIn.checkNoEntityCollision(aabb.offset(pos)) && worldIn.setBlockState(pos, doubleState, 11)) {
                        SoundType sound = this.doubleSlab.getSoundType(doubleState, worldIn, pos, player);
                        worldIn.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
                        if (!player.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }

                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(player, stack, worldIn, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.FAIL;
    }

    private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() == this.halfSlab) {
            IBlockState doubleState = doubleSlab.getDefaultState();
            AxisAlignedBB aabb = doubleState.getCollisionBoundingBox(worldIn, pos);
            if (aabb != Block.NULL_AABB && worldIn.checkNoEntityCollision(aabb.offset(pos)) && worldIn.setBlockState(pos, doubleState, 11)) {
                SoundType sound = this.doubleSlab.getSoundType(doubleState, worldIn, pos, player);
                worldIn.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return true;
        }
        return false;
    }
}
