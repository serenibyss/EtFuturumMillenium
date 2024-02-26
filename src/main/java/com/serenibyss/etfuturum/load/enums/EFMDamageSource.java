package com.serenibyss.etfuturum.load.enums;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import javax.annotation.Nullable;

public class EFMDamageSource {

    public static DamageSource causeTridentDamage(Entity source, @Nullable Entity indirectEntityIn) {
        return (new EntityDamageSourceIndirect("trident", source, indirectEntityIn)).setProjectile();
    }
}
