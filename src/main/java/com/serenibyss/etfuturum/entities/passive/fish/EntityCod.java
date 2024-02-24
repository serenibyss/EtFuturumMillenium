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

public class EntityCod extends AbstractGroupFish{
    public EntityCod(World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.3f);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return EFMLootTables.ENTITIES_COD;
    }

    @Override
    protected ItemStack getFishBucket() {
        return EFMItems.COD_BUCKET.getItemStack();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return EFMSounds.ENTITY_COD_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return EFMSounds.ENTITY_COD_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return EFMSounds.ENTITY_COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return EFMSounds.ENTITY_COD_FLOP;
    }
}
