package com.serenibyss.etfuturum.entities.animal;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EntityTurtle extends EntityAnimal {

    private static final DataParameter<BlockPos> HOME_POS;

    public EntityTurtle(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 0.4f);

        this.spawnableBlock  = Blocks.SAND;
        this.stepHeight = 1.0f;
    }

    public void setHome(BlockPos pos) {
        this.dataManager.set(HOME_POS, pos);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HOME_POS, BlockPos.ORIGIN);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    static {
        HOME_POS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BLOCK_POS);
    }
}
