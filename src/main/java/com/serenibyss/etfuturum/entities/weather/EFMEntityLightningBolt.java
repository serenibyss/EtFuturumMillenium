package com.serenibyss.etfuturum.entities.weather;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EFMEntityLightningBolt extends EntityLightningBolt {

    private EntityPlayerMP caster;

    public EFMEntityLightningBolt(World worldIn, double x, double y, double z, boolean effectOnlyIn) {
        super(worldIn, x, y, z, effectOnlyIn);
    }

    public void setCaster(EntityPlayerMP caster) {
        this.caster = caster;
    }

    @Nullable
    public EntityPlayerMP getCaster() {
        return caster;
    }
}
