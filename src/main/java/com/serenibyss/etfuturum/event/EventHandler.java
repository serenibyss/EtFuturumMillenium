package com.serenibyss.etfuturum.event;

import com.serenibyss.etfuturum.stats.EFMStatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
}
