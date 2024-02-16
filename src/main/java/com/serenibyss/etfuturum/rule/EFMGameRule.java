package com.serenibyss.etfuturum.rule;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EFMGameRule {

    private final Feature feature;
    private final String name;
    private final String defaultValue;
    private final GameRules.ValueType type;
    private boolean registered = false;

    protected EFMGameRule(Feature feature, String name, String defaultValue, GameRules.ValueType type) {
        this.feature = feature;
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    protected void register(World world) {
        if (feature.isEnabled() && world != null) {
            world.getGameRules().addGameRule(name, defaultValue, type);
            this.registered = true;
        }
    }

    public String getString(World world) {
        if (!registered) register(world);
        if (!registered) return "";
        return world.getGameRules().getString(name);
    }

    public boolean getBoolean(World world) {
        if (!registered) register(world);
        if (!registered) return false;
        return world.getGameRules().getBoolean(name);
    }

    public int getInt(World world) {
        if (!registered) register(world);
        if (!registered) return 0;
        return world.getGameRules().getInt(name);
    }
}
