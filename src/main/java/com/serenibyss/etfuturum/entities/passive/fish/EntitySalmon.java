package com.serenibyss.etfuturum.entities.passive.fish;

import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.loot.EFMLootTables;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EntitySalmon extends AbstractGroupFish {

    public EntitySalmon(World worldIn) {
        super(worldIn);
        setSize(0.7f, 0.4f);
    }

    @Override
    public int getMaxGroupSize() {
        return 5;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return EFMLootTables.ENTITIES_SALMON;
    }

    @Override
    protected ItemStack getFishBucket() {
        return EFMItems.SALMON_BUCKET.getItemStack();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return EFMSounds.ENTITY_SALMON_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return EFMSounds.ENTITY_SALMON_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return EFMSounds.ENTITY_SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return EFMSounds.ENTITY_SALMON_FLOP;
    }
}
