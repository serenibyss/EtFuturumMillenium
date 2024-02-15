package com.serenibyss.etfuturum.event;

import com.serenibyss.etfuturum.stats.EFMStatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        event.getEntityPlayer().takeStat(EFMStatList.TIME_SINCE_REST);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer player) {
            player.takeStat(EFMStatList.TIME_SINCE_REST);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.addStat(EFMStatList.TIME_SINCE_REST);
    }
}
