package com.serenibyss.etfuturum.mixin.trident;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityArrow.class)
public interface EntityArrowAccessor {

    @Accessor
    public DataParameter<Byte> getCRITICAL();

    @Accessor
    public int getTicksInGround();
    @Accessor("ticksInGround")
    public void setTicksInGround(int ticksInGround);

    @Accessor
    public int getTicksInAir();
    @Accessor("ticksInAir")
    public void setTicksInAir(int ticksInAir);

    @Accessor
    public Block getInTile();

    @Accessor
    public int getInData();

    @Accessor
    public int getXTile();

    @Accessor
    public int getYTile();

    @Accessor
    public int getZTile();
}
