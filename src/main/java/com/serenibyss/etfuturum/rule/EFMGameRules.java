package com.serenibyss.etfuturum.rule;

import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMGameRules {

    public static EFMGameRule DO_INSOMNIA;

    @SubscribeEvent
    public static void initRules(WorldEvent.Load event) {
        DO_INSOMNIA = new EFMGameRule(MC13.phantom, "doInsomnia", "true", ValueType.BOOLEAN_VALUE);
    }
}
