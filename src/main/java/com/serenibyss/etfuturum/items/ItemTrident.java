package com.serenibyss.etfuturum.items;

import com.google.common.collect.Multimap;
import com.serenibyss.etfuturum.EtFuturum;
import com.serenibyss.etfuturum.enchantment.EFMEnchantments;
import com.serenibyss.etfuturum.entities.projectile.EntityTrident;
import com.serenibyss.etfuturum.load.enums.EFMEnumAction;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.IModelRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemTrident extends Item implements IModelRegister {

    public ItemTrident() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(250);

        this.addPropertyOverride(new ResourceLocation("throwing"), (stack, world, entity) ->
                entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0f : 0.0f);
        setCreativeTab(CreativeTabs.BREWING);
        setTranslationKey("trident");
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EFMEnumAction.SPEAR;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    public void registerModel() {
        EtFuturum.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if(entityLiving instanceof EntityPlayer player) {
            int duration = this.getMaxItemUseDuration(stack) - timeLeft;
            if(duration >= 10) {
                int riptide = EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.RIPTIDE, stack);
                if(riptide <= 0 || player.isWet()) {
                    if(!worldIn.isRemote) {
                        stack.damageItem(1, player);
                        if(riptide == 0) {
                            EntityTrident trident = new EntityTrident(worldIn, player, stack);
                            trident.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 2.5F + (float)riptide * 0.5F, 1.0F);
                            if(player.isCreative())
                                trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                            worldIn.spawnEntity(trident);
                            if(!player.isCreative())
                                player.inventory.deleteStack(stack);
                        }
                    }
                    player.addStat(StatList.getObjectUseStats(this));
                    SoundEvent sound = EFMSounds.ITEM_TRIDENT_THROW;
                    if(riptide > 0) {
                        float x = -MathHelper.sin((float)Math.toRadians(player.rotationYaw)) * MathHelper.cos((float)Math.toRadians(player.rotationPitch));
                        float y = -MathHelper.sin((float)Math.toRadians(player.rotationPitch));
                        float z = MathHelper.cos((float)Math.toRadians(player.rotationYaw)) * MathHelper.cos((float)Math.toRadians(player.rotationPitch));
                        float mag = MathHelper.sqrt(x * x + y * y + z * z);
                        float height = 3.0F * ((1.0f + (float)riptide) / 4.0f);
                        x *= height / mag;
                        y *= height / mag;
                        z *= height / mag;
                        player.addVelocity(x, y, z);
                        if(riptide >= 3) {
                            sound = EFMSounds.ITEM_TRIDENT_RIPTIDE_3;
                        } else if(riptide == 2) {
                            sound = EFMSounds.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            sound = EFMSounds.ITEM_TRIDENT_RIPTIDE_1;
                        }

                        if(player.onGround) {
                            player.move(MoverType.SELF, 0.0, 1.2, 0.0);
                        }
                    }

                    worldIn.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(stack.getItemDamage() > stack.getMaxDamage()) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        } else if(EnchantmentHelper.getEnchantmentLevel(EFMEnchantments.RIPTIDE, stack) > 0 && !playerIn.isWet()) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if((double)state.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock() == Blocks.WEB;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> attribs = super.getAttributeModifiers(slot, stack);
        if(slot == EntityEquipmentSlot.MAINHAND) {
            attribs.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 8.0, 0));
            attribs.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.9, 0));
        }

        return attribs;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
