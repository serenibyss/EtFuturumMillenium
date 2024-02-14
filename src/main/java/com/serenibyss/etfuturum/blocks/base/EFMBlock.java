package com.serenibyss.etfuturum.blocks.base;

import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.util.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("deprecation")
public class EFMBlock extends Block {

    protected final Settings settings;

    public EFMBlock(Settings settings) {
        super(settings.material, settings.material.getMaterialMapColor());
        this.settings = settings;

        setResistance(settings.resistance);
        setHardness(settings.hardness);
        setSoundType(settings.soundType);
        setTranslationKey(settings.translationKey);
        setCreativeTab(settings.tab);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return settings.opaque;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return settings.fullCube;
    }

    @Override
    public boolean isCollidable() {
        return settings.collidable;
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

    @ApiStatus.Internal
    public void registerModel() {
        EtFuturum.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    public static VoxelShape createShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new VoxelShape(x1, y1, z1, x2, y2, z2);
    }

    /** Created to be similar to modern MC's way of defining block properties */
    public static class Settings {

        final Material material;
        final ContextFunction<MapColor> mapColor;
        String translationKey;
        CreativeTabs tab;
        boolean collidable = true;
        boolean opaque = true;
        boolean fullCube = true;
        float resistance;
        float hardness;
        SoundType soundType = SoundType.STONE;
        ContextFunction<Integer> lightValue = (state, world, pos) -> 0;
        ContextFunction<Float> slipperiness = (state, world, pos) -> 0.6F;

        public Settings(Material material) {
            this(material, material.getMaterialMapColor());
        }

        public Settings(Material material, MapColor mapColor) {
            this(material, (state, world, pos) -> mapColor);
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

        public interface ContextFunction<R> {
            R apply(IBlockState state, IBlockAccess world, BlockPos pos);
        }
    }
}
