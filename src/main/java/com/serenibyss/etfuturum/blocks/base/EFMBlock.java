package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.util.IModelRegister;
import com.serenibyss.etfuturum.util.ModIDs;
import com.serenibyss.etfuturum.util.VoxelShape;
import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("deprecation")
@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.api.block.IFluidloggable", modid = ModIDs.FLUIDLOGGED)
public class EFMBlock extends Block implements IModelRegister, IFluidloggable, IMultiItemBlock {

    protected final Settings settings;

    public EFMBlock(Settings settings) {
        super(settings.material, settings.material.getMaterialMapColor());
        this.settings = settings;

        setResistance(settings.resistance);
        setHardness(settings.hardness);
        setSoundType(settings.soundType);
        setTranslationKey(settings.translationKey);
        setCreativeTab(settings.tab);

        // Fix some potential issues with these fields being set prematurely by the super ctor
        this.fullBlock = getDefaultState().isOpaqueCube();
        this.lightOpacity = this.fullBlock ? 255 : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return settings != null && settings.opaque;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return settings.fullCube;
    }

    @Override
    public boolean isCollidable() {
        return settings.collidable;
    }

    public boolean getHasItemSubtypes() {
        return settings.hasItemSubtypes;
    }

    @Override
    public String getTranslationKey(int meta) {
        return this.getTranslationKey();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return isOpaqueCube(state) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        return settings.slipperiness.apply(state, world, pos);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return settings.lightValue.apply(state, world, pos);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return settings.mapColor != null
                ? settings.mapColor.apply(state, world, pos)
                : settings.material.getMaterialMapColor();
    }

    public Item asItem() {
        return Item.getItemFromBlock(this);
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public final boolean isFluidloggable(IBlockState state, World world, BlockPos pos) {
        return isWaterloggable(state, world, pos);
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public final boolean isFluidValid(IBlockState state, World world, BlockPos pos, Fluid fluid) {
        return isWaterloggable(state, world, pos) && fluid == FluidRegistry.WATER;
    }

    @Optional.Method(modid = ModIDs.FLUIDLOGGED)
    @Override
    public final boolean canFluidFlow(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EnumFacing side) {
        return isWaterloggable(state, world, pos) && canWaterFlow(world, pos, state, side);
    }

    /** Whether this block can be water-logged or not. */
    public boolean isWaterloggable(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    /** Whether water can flow into/out of this block. */
    public boolean canWaterFlow(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
        return state.getBlockFaceShape(world, pos, side) != BlockFaceShape.SOLID;
    }

    public static VoxelShape createShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new VoxelShape(x1, y1, z1, x2, y2, z2);
    }

    /** Created to be similar to modern MC's way of defining block properties */
    public static class Settings {

        public final Material material;
        public ContextFunction<MapColor> mapColor;
        public String translationKey;
        public CreativeTabs tab;
        public boolean collidable = true;
        public boolean opaque = true;
        public boolean fullCube = true;
        public boolean hasItemSubtypes = false;
        public float resistance;
        public float hardness;
        public SoundType soundType = SoundType.STONE;
        public ContextFunction<Integer> lightValue = (state, world, pos) -> 0;
        public ContextFunction<Float> slipperiness = (state, world, pos) -> 0.6F;
        public EnumRarity rarity;

        public Settings(Material material) {
            this(material, material.getMaterialMapColor());
        }

        public Settings(Material material, MapColor mapColor) {
            this(material, (state, world, pos) -> mapColor);
        }

        public static Settings from(Block blockIn) {
            return from(blockIn, 0);
        }

        public static Settings from(Block blockIn, int meta) {
            IBlockState state = blockIn.getStateFromMeta(meta);
            Settings settings = new Settings(blockIn.getMaterial(state));
            settings.mapColor = ($, world, pos) -> blockIn.getMapColor(state, world, pos);
            settings.tab = blockIn.getCreativeTab();
            settings.collidable = blockIn.isCollidable();
            settings.opaque = blockIn.isOpaqueCube(state);
            settings.fullCube = blockIn.isFullCube(state);
            settings.resistance = blockIn.getExplosionResistance(null) * 5.0F / 3.0F;
            settings.hardness = blockIn.getBlockHardness(null, null, null);
            settings.soundType = blockIn.getSoundType();
            settings.lightValue = ($, world, pos) -> blockIn.getLightValue(state, world, pos);
            settings.slipperiness = ($, world, pos) -> blockIn.getSlipperiness(state, world, pos, null);
            return settings;
        }

        public Settings(Material material, ContextFunction<MapColor> mapColor) {
            this.material = material;
            this.mapColor = mapColor;
        }

        public Settings noCollision() {
            collidable = false;
            opaque = false;
            return this;
        }

        public Settings nonOpaque() {
            opaque = false;
            return this;
        }

        public Settings nonFullCube() {
            fullCube = false;
            return this;
        }

        public Settings translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Settings creativeTab(CreativeTabs tab) {
            this.tab = tab;
            return this;
        }

        public Settings strength(float strength) {
            resistance = strength;
            hardness = strength;
            return this;
        }

        public Settings resistance(float resistance) {
            this.resistance = resistance;
            return this;
        }

        public Settings hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        public Settings soundType(SoundType soundType) {
            this.soundType = soundType;
            return this;
        }

        public Settings lightValue(ContextFunction<Integer> lightValue) {
            this.lightValue = lightValue;
            return this;
        }

        public Settings lightValue(Function<IBlockState, Integer> lightValue) {
            this.lightValue = (state, access, pos) -> lightValue.apply(state);
            return this;
        }

        public Settings lightValue(int lightValue) {
            this.lightValue = (state, access, pos) -> lightValue;
            return this;
        }

        public Settings slipperiness(ContextFunction<Float> slipperiness) {
            this.slipperiness = slipperiness;
            return this;
        }

        public Settings slipperiness(Function<IBlockState, Float> slipperiness) {
            this.slipperiness = (state, access, pos) -> slipperiness.apply(state);
            return this;
        }

        public Settings slipperiness(float slipperiness) {
            this.slipperiness = (state, access, pos) -> slipperiness;
            return this;
        }

        public Settings hasItemSubtypes() {
            this.hasItemSubtypes = true;
            return this;
        }

        public Settings rarity(EnumRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public interface ContextFunction<R> {
            R apply(IBlockState state, IBlockAccess world, BlockPos pos);
        }
    }
}
