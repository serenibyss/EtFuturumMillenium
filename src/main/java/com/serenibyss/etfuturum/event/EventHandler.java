package com.serenibyss.etfuturum.event;

import com.serenibyss.etfuturum.api.StrippingRegistry;
import com.serenibyss.etfuturum.advancement.EFMAdvancements;
import com.serenibyss.etfuturum.entities.weather.EFMEntityLightningBolt;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.stats.EFMStatList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.serenibyss.etfuturum.load.feature.Features.MC13;

public class EventHandler {

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        final EntityPlayer player = event.getEntityPlayer();

        if (MC13.phantom.isEnabled()) {
            player.takeStat(EFMStatList.TIME_SINCE_REST);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();

        if (MC13.phantom.isEnabled()) {
            if (entity instanceof EntityPlayer player) {
                player.takeStat(EFMStatList.TIME_SINCE_REST);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        final EntityPlayer player = event.player;

        if (MC13.phantom.isEnabled()) {
            player.addStat(EFMStatList.TIME_SINCE_REST);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack usedItem = event.getItemStack();
        if (MC13.stripping.isEnabled() && usedItem.getItem().getToolClasses(usedItem).contains("axe")) {
            BlockPos pos = event.getPos();
            World world = event.getWorld();
            IBlockState state = world.getBlockState(pos);
            StrippingRegistry reg = StrippingRegistry.instance();
            IBlockState converted = reg.getStrippedBlockState(state);
            if (converted != null) {
                world.playSound(event.getEntityPlayer(), pos, EFMSounds.ITEM_AXE_STRIP_LOG, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote) {
                    world.setBlockState(pos, converted, 0b1011);
                    event.setCancellationResult(EnumActionResult.SUCCESS);
                    event.setCanceled(true);
                    if (event.getEntityPlayer() != null) {
                        usedItem.damageItem(1, event.getEntityPlayer());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLightningHitEntity(EntityStruckByLightningEvent event) {
        final EntityLightningBolt lightning = event.getLightning();
        final Entity hitEntity = event.getEntity();

        if (MC13.trident.isEnabled()) {
            if (lightning instanceof EFMEntityLightningBolt efmLightning) {
                EntityPlayerMP player = efmLightning.getCaster();
                if (player != null && hitEntity != null) {
                    EFMAdvancements.TRIDENT_CHANNELING.trigger(player, hitEntity);
                }
            }
        }
    }
}
