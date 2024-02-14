package com.serenibyss.etfuturum.sounds;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class EFMSounds {

    public static SoundEvent BARREL_OPEN;
    public static SoundEvent BARREL_CLOSE;
    public static SoundEvent UI_STONECUTTER_TAKE_RESULT;

    public static SoundEvent ENTITY_PHANTOM_AMBIENT;
    public static SoundEvent ENTITY_PHANTOM_ATTACK;
    public static SoundEvent ENTITY_PHANTOM_BITE;
    public static SoundEvent ENTITY_PHANTOM_DEATH;
    public static SoundEvent ENTITY_PHANTOM_FLAP;
    public static SoundEvent ENTITY_PHANTOM_HURT;
    public static SoundEvent ENTITY_PHANTOM_SWOOP;

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> r = event.getRegistry();

        if(Features.MC13.phantom.isEnabled()) {
            ENTITY_PHANTOM_AMBIENT = register(r, "entity.phantom.ambient");
            ENTITY_PHANTOM_ATTACK = register(r, "entity.phantom.attack");
            ENTITY_PHANTOM_BITE = register(r, "entity.phantom.bite");
            ENTITY_PHANTOM_DEATH = register(r, "entity.phantom.death");
            ENTITY_PHANTOM_FLAP = register(r, "entity.phantom.flap");
            ENTITY_PHANTOM_HURT = register(r, "entity.phantom.hurt");
            ENTITY_PHANTOM_SWOOP = register(r, "entity.phantom.swoop");
        }

        if (Features.MC14.barrel.isEnabled()) {
            BARREL_OPEN = register(r, "block.barrel.open");
            BARREL_CLOSE = register(r, "block.barrel.close");
        }

        if (Features.MC14.stonecutter.isEnabled()) {
            UI_STONECUTTER_TAKE_RESULT = register(r, "ui.stonecutter.take_result");
        }
    }

    private static SoundEvent register(IForgeRegistry<SoundEvent> registry, String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(EFMTags.MODID, name));
        event.setRegistryName(event.getSoundName());
        registry.register(event);
        return event;
    }
}
