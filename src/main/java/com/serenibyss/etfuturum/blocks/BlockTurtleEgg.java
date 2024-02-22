package com.serenibyss.etfuturum.blocks;

import com.serenibyss.etfuturum.blocks.base.EFMBlock;
import com.serenibyss.etfuturum.entities.passive.EntityTurtle;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockTurtleEgg extends EFMBlock {

    private static final VoxelShape ONE_EGG_SHAPE = createShape(3, 0, 3, 12, 7, 12);
    private static final VoxelShape MULTI_EGG_SHAPE = createShape(1, 0, 1, 15, 7, 15);

    public static final PropertyInteger HATCH = PropertyInteger.create("hatch", 0, 2);
    public static final PropertyInteger EGGS = PropertyInteger.create("eggs", 1, 4);

    public BlockTurtleEgg() {
        super(new Settings(Material.DRAGON_EGG, MapColor.SAND)
                .hardness(0.5f)
                .resistance(0.5f)
                .translationKey("turtle_egg")
                .soundType(SoundType.METAL)
                .creativeTab(CreativeTabs.MISC)
                .nonOpaque()
                .nonFullCube());
        setDefaultState(getBlockState().getBaseState().withProperty(EGGS, 1).withProperty(HATCH, 0));
        setTickRandomly(true);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        tryTrample(worldIn, pos, entityIn, 100);
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EGGS, HATCH);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if(!(entityIn instanceof EntityZombie)) {
            tryTrample(worldIn, pos, entityIn, 3);
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    private void tryTrample(World world, BlockPos pos, Entity entity, int chance) {
        if (!isTurtle(world, entity)) {
            super.onEntityWalk(world, pos, entity);
        } else {
            if(!world.isRemote && world.rand.nextInt(chance) == 0) {
                removeOneEgg(world, pos, world.getBlockState(pos));
            }
        }
    }

    private void removeOneEgg(World world, BlockPos pos, IBlockState blockState) {
        world.playSound(null, pos, EFMSounds.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7f, 0.9f + world.rand.nextFloat() * 0.2f);
        int i = blockState.getValue(EGGS);
        if (i <= 1) {
            world.destroyBlock(pos, false);
        } else {
            world.setBlockState(pos, blockState.withProperty(EGGS, i - 1), 2);
            world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(blockState));
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.canGrow(worldIn) && hasProperHabitat(worldIn, pos)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                worldIn.playSound(null, pos, EFMSounds.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + rand.nextFloat() * 0.2F);
                worldIn.setBlockState(pos, state.withProperty(HATCH, i + 1), 2);
            } else {
                worldIn.playSound(null, pos, EFMSounds.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + rand.nextFloat() * 0.2F);
                worldIn.destroyBlock(pos, false);
                if (!worldIn.isRemote) {
                    for (int l = 0; l < state.getValue(EGGS); l++) {
                        worldIn.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(state));
                        EntityTurtle turtle = new EntityTurtle(worldIn);
                        turtle.setGrowingAge(-24000);
                        turtle.setHome(pos);
                        turtle.setLocationAndAngles(pos.getX() + 0.3 + l * 0.2, pos.getY(), pos.getZ() + 0.3, 0.0f, 0.0f);
                        worldIn.spawnEntity(turtle);
                    }
                }
            }
        }
    }

    private boolean hasProperHabitat(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() == Blocks.SAND;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (this.hasProperHabitat(worldIn, pos) && !worldIn.isRemote) {
            worldIn.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
        }
    }

    private boolean canGrow(World world) {
        float f = world.getCelestialAngle(1.0f);
        if (f > 0.65 && f < 0.69) {
            return true;
        }
        return world.rand.nextInt(500) == 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        this.removeOneEgg(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(this)) {
            int egg = state.getValue(EGGS);
            if (egg < 4) {
                if (!worldIn.isRemote) {
                    worldIn.setBlockState(pos, state.withProperty(EGGS, egg + 1), 3);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this ? state.withProperty(EGGS, Math.min(4, state.getValue(EGGS) + 1)) : super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HATCH) | ((state.getValue(EGGS) - 1) << 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HATCH, meta & 3).withProperty(EGGS, ((meta >> 2) & 3) + 1);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(EGGS) > 1 ? MULTI_EGG_SHAPE : ONE_EGG_SHAPE;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(EGGS) > 1 ? MULTI_EGG_SHAPE : ONE_EGG_SHAPE;
    }

    private boolean isTurtle(World world, Entity entity) {
        if (entity instanceof EntityTurtle) return false;
        if (entity instanceof EntityPlayer) return true;
        return entity instanceof EntityLivingBase && ForgeEventFactory.getMobGriefingEvent(world, entity);
    }
}
