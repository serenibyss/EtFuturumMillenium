package com.serenibyss.etfuturum.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.stats.StatList;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistry;

public class EFMStatList {

    public static final StatBase TIME_SINCE_REST = (new StatBasic("stat.timeSinceRest", new TextComponentTranslation("stat.timeSinceRest", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();

}
