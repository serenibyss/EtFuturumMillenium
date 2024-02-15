package com.serenibyss.etfuturum.load;

import com.google.common.collect.ImmutableList;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

@SuppressWarnings("unused")
public class EtFuturumLateMixinLoader implements ILateMixinLoader {

    static boolean atLateStage = false;

    @Override
    public List<String> getMixinConfigs() {
        atLateStage = true;
        return ImmutableList.of("mixins.etfuturum.late.json");
    }
}
