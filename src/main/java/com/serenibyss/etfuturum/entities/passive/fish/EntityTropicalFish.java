package com.serenibyss.etfuturum.entities.passive.fish;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.items.EFMItems;
import com.serenibyss.etfuturum.loot.EFMLootTables;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EntityTropicalFish extends AbstractGroupFish {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityTropicalFish.class, DataSerializers.VARINT);

    private static final ResourceLocation[] BODY_TEXTURES = {
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b.png"), };

    private static final ResourceLocation[] PATTERN_TEXTURES_A = {
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_1.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_2.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_3.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_4.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_5.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_a_pattern_6.png"), };

    private static final ResourceLocation[] PATTERN_TEXTURES_B = {
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_1.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_2.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_3.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_4.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_5.png"),
            new ResourceLocation(EFMTags.MODID, "textures/entity/fish/tropical_b_pattern_6.png"), };

    public static final int[] SPECIAL_VARIANTS = {
            pack(EntityTropicalFish.Type.STRIPEY, EnumDyeColor.ORANGE, EnumDyeColor.GRAY),
            pack(EntityTropicalFish.Type.FLOPPER, EnumDyeColor.GRAY, EnumDyeColor.GRAY),
            pack(EntityTropicalFish.Type.FLOPPER, EnumDyeColor.GRAY, EnumDyeColor.BLUE),
            pack(EntityTropicalFish.Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.GRAY),
            pack(EntityTropicalFish.Type.SUNSTREAK, EnumDyeColor.BLUE, EnumDyeColor.GRAY),
            pack(EntityTropicalFish.Type.KOB, EnumDyeColor.ORANGE, EnumDyeColor.WHITE),
            pack(EntityTropicalFish.Type.SPOTTY, EnumDyeColor.PINK, EnumDyeColor.LIGHT_BLUE),
            pack(EntityTropicalFish.Type.BLOCKFISH, EnumDyeColor.PURPLE, EnumDyeColor.YELLOW),
            pack(EntityTropicalFish.Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.RED),
            pack(EntityTropicalFish.Type.SPOTTY, EnumDyeColor.WHITE, EnumDyeColor.YELLOW),
            pack(EntityTropicalFish.Type.GLITTER, EnumDyeColor.WHITE, EnumDyeColor.GRAY),
            pack(EntityTropicalFish.Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.ORANGE),
            pack(EntityTropicalFish.Type.DASHER, EnumDyeColor.CYAN, EnumDyeColor.PINK),
            pack(EntityTropicalFish.Type.BRINELY, EnumDyeColor.LIME, EnumDyeColor.LIGHT_BLUE),
            pack(EntityTropicalFish.Type.BETTY, EnumDyeColor.RED, EnumDyeColor.WHITE),
            pack(EntityTropicalFish.Type.SNOOPER, EnumDyeColor.GRAY, EnumDyeColor.RED),
            pack(EntityTropicalFish.Type.BLOCKFISH, EnumDyeColor.RED, EnumDyeColor.WHITE),
            pack(EntityTropicalFish.Type.FLOPPER, EnumDyeColor.WHITE, EnumDyeColor.YELLOW),
            pack(EntityTropicalFish.Type.KOB, EnumDyeColor.RED, EnumDyeColor.WHITE),
            pack(EntityTropicalFish.Type.SUNSTREAK, EnumDyeColor.GRAY, EnumDyeColor.WHITE),
            pack(EntityTropicalFish.Type.DASHER, EnumDyeColor.CYAN, EnumDyeColor.YELLOW),
            pack(EntityTropicalFish.Type.FLOPPER, EnumDyeColor.YELLOW, EnumDyeColor.YELLOW), };

    private boolean isSchool = true;

    private static int pack(EntityTropicalFish.Type size, EnumDyeColor pattern, EnumDyeColor bodyColor) {
        return size.getBase() & 0xFF
                | (size.getIndex() & 0xFF) << 8
                | (pattern.getMetadata() & 0xFF) << 16
                | (bodyColor.getMetadata() & 0xFF) << 24;
    }

    @SuppressWarnings("unused")
    public EntityTropicalFish(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.4f);
    }

    public EntityTropicalFish(World worldIn, ItemStack fromBucket) {
        super(worldIn);
        this.setSize(0.5f, 0.4f);

        NBTTagCompound tag = fromBucket.getTagCompound();
        if (tag != null) {
            this.setVariant(tag.getInteger("Variant"));
        }
    }

    public static String getPredefinedName(int index) {
        return "entity.tropical_fish.predefined." + index;
    }

    public static EnumDyeColor getBaseColor(int index) {
        return EnumDyeColor.byMetadata(getBodyColorID(index));
    }

    public static EnumDyeColor getPatternColor(int index) {
        return EnumDyeColor.byMetadata(getPatternColorID(index));
    }

    public static String getFishTypeName(int index) {
        int i = getBaseVariant(index);
        int j = getPatternVariant(index);
        return "entity.tropical_fish.type" + EntityTropicalFish.Type.getTypeName(i, j);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(VARIANT, 0);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return EFMLootTables.ENTITIES_TROPICAL_FISH;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    // todo this needs to still be injected via mixin/asm
    public boolean inSchool() {
        return !this.isSchool;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Override
    protected void setBucketData(ItemStack stack) {
        super.setBucketData(stack);
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null || compound.isEmpty()) {
            compound = new NBTTagCompound();
            compound.setInteger("BucketVariantTag", this.getVariant());
            stack.setTagCompound(compound);
        } else {
            compound.setInteger("BucketVariantTag", this.getVariant());
        }
    }

    @Override
    protected ItemStack getFishBucket() {
        return EFMItems.TROPICAL_FISH_BUCKET.getItemStack();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return EFMSounds.ENTITY_TROPICAL_FISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return EFMSounds.ENTITY_TROPICAL_FISH_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return EFMSounds.ENTITY_TROPICAL_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return EFMSounds.ENTITY_TROPICAL_FISH_FLOP;
    }

    private static int getBodyColorID(int index) {
        return (index & 0xFF0000) >> 16;
    }

    public float[] getBodyColorTexture() {
        return EnumDyeColor.byMetadata(getBodyColorID(this.getVariant())).getColorComponentValues();
    }

    private static int getPatternColorID(int index) {
        return (index & 0xFF000000) >> 24;
    }

    public float[] getPatternColorTexture() {
        return EnumDyeColor.byMetadata(getPatternColorID(this.getVariant())).getColorComponentValues();
    }

    public static int getBaseVariant(int index) {
        return Math.min(index & 0xFF, 1);
    }

    public int getBaseVariant() {
        return getBaseVariant(this.getVariant());
    }

    private static int getPatternVariant(int index) {
        return Math.min((index & 0xFF00) >> 8, 5);
    }

    public ResourceLocation getPatternTexture() {
        return getBaseVariant(this.getVariant()) == 0 ? PATTERN_TEXTURES_A[getPatternVariant(this.getVariant())] : PATTERN_TEXTURES_B[getPatternVariant(this.getVariant())];
    }

    public ResourceLocation getBodyTexture() {
        return BODY_TEXTURES[getBaseVariant(this.getVariant())];
    }

    @Override
    public @Nullable IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i;
        int j;
        int k;
        int l;
        if(livingdata instanceof EntityTropicalFish.GroupData tfld) {
            i = tfld.base;
            j = tfld.pattern;
            k = tfld.bodyColor;
            l = tfld.patternColor;
        } else if((double) this.rand.nextFloat() < 0.9) {
            int i1 = SPECIAL_VARIANTS[this.rand.nextInt(SPECIAL_VARIANTS.length)];
            i = i1 & 0xFF;
            j = (i1 & 0xFF00) >> 8;
            k = (i1 & 0xFF0000) >> 16;
            l = (i1 & 0xFF000000) >> 24;
            livingdata = new EntityTropicalFish.GroupData(this, i, j, k, l);
        } else {
            this.isSchool = false;
            i = this.rand.nextInt(2);
            j = this.rand.nextInt(6);
            k = this.rand.nextInt(15);
            l = this.rand.nextInt(15);
        }

        this.setVariant(i | j << 8 | k << 16 | l << 24);
        return livingdata;
    }

    public static class GroupData extends AbstractGroupFish.GroupData {
        private final int base;
        private final int pattern;
        private final int bodyColor;
        private final int patternColor;

        public GroupData(AbstractGroupFish group, int base, int pattern, int bodyColor, int patternColor) {
            super(group);
            this.base = base;
            this.pattern = pattern;
            this.bodyColor = bodyColor;
            this.patternColor = patternColor;
        }
    }

    enum Type {
        KOB(0, 0),
        SUNSTREAK(0, 1),
        SNOOPER(0, 2),
        DASHER(0, 3),
        BRINELY(0, 4),
        SPOTTY(0, 5),
        FLOPPER(1, 0),
        STRIPEY(1, 1),
        GLITTER(1, 2),
        BLOCKFISH(1, 3),
        BETTY(1, 4),
        CLAYFISH(1, 5);

        private final int base;
        private final int index;
        private static final EntityTropicalFish.Type[] VALUES = values();

        Type(int base, int index) {
            this.base = base;
            this.index = index;
        }

        public int getBase() {
            return this.base;
        }

        public int getIndex() {
            return this.index;
        }

        public static String getTypeName(int base, int index) {
            return VALUES[index + 6 * base].getName();
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
