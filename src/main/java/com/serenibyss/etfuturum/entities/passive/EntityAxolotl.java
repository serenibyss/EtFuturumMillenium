package com.serenibyss.etfuturum.entities.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EntityAxolotl extends EntityAnimal {
    public EntityAxolotl(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }
}
