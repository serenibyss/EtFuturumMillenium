package com.serenibyss.etfuturum.sounds;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.serenibyss.etfuturum.load.feature.Features.MC13;
import static com.serenibyss.etfuturum.load.feature.Features.MC14;

public class EFMSounds {

    public static SoundEvent BARREL_OPEN;
    public static SoundEvent BARREL_CLOSE;
    public static SoundEvent UI_STONECUTTER_TAKE_RESULT;

    public static SoundEvent ENTITY_PHANTOM_AMBIENT;
    public static SoundEvent ENTITY_PHANTOM_BITE;
    public static SoundEvent ENTITY_PHANTOM_DEATH;
    public static SoundEvent ENTITY_PHANTOM_FLAP;
    public static SoundEvent ENTITY_PHANTOM_HURT;
    public static SoundEvent ENTITY_PHANTOM_SWOOP;

    public static SoundEvent BLOCK_CONDUIT_AMBIENT;
    public static SoundEvent BLOCK_CONDUIT_AMBIENT_SHORT;
    public static SoundEvent BLOCK_CONDUIT_ATTACK_TARGET;
    public static SoundEvent BLOCK_CONDUIT_ACTIVATE;
    public static SoundEvent BLOCK_CONDUIT_DEACTIVATE;

    public static SoundEvent ITEM_AXE_STRIP_LOG;

    public static SoundEvent ITEM_TRIDENT_HIT;
    public static SoundEvent ITEM_TRIDENT_RETURN;
    public static SoundEvent ITEM_TRIDENT_THUNDER;
    public static SoundEvent ITEM_TRIDENT_THROW;
    public static SoundEvent ITEM_TRIDENT_HIT_GROUND;
    public static SoundEvent ITEM_TRIDENT_RIPTIDE_1;
    public static SoundEvent ITEM_TRIDENT_RIPTIDE_2;
    public static SoundEvent ITEM_TRIDENT_RIPTIDE_3;

    public static SoundEvent ENTITY_TURTLE_AMBIENT_LAND;
    public static SoundEvent ENTITY_TURTLE_SWIM;
    public static SoundEvent ENTITY_TURTLE_HURT;
    public static SoundEvent ENTITY_TURTLE_HURT_BABY;
    public static SoundEvent ENTITY_TURTLE_DEATH;
    public static SoundEvent ENTITY_TURTLE_DEATH_BABY;
    public static SoundEvent ENTITY_TURTLE_SHAMBLE;
    public static SoundEvent ENTITY_TURTLE_SHAMBLE_BABY;

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> r = event.getRegistry();

        ENTITY_PHANTOM_AMBIENT = register(MC13.phantom, r, "entity.phantom.ambient");
        ENTITY_PHANTOM_BITE = register(MC13.phantom, r, "entity.phantom.bite");
        ENTITY_PHANTOM_DEATH = register(MC13.phantom, r, "entity.phantom.death");
        ENTITY_PHANTOM_FLAP = register(MC13.phantom, r, "entity.phantom.flap");
        ENTITY_PHANTOM_HURT = register(MC13.phantom, r, "entity.phantom.hurt");
        ENTITY_PHANTOM_SWOOP = register(MC13.phantom, r, "entity.phantom.swoop");

        BLOCK_CONDUIT_AMBIENT = register(MC13.conduit, r, "block.conduit.ambient");
        BLOCK_CONDUIT_AMBIENT_SHORT = register(MC13.conduit, r, "block.conduit.ambient.short");
        BLOCK_CONDUIT_ATTACK_TARGET = register(MC13.conduit, r, "block.conduit.attack.target");
        BLOCK_CONDUIT_ACTIVATE = register(MC13.conduit, r, "block.conduit.activate");
        BLOCK_CONDUIT_DEACTIVATE = register(MC13.conduit, r, "block.conduit.deactivate");

        ITEM_AXE_STRIP_LOG = register(MC13.stripping, r, "item.axe.strip");

        ITEM_TRIDENT_HIT = register(MC13.trident, r, "item.trident.hit");
        ITEM_TRIDENT_RETURN = register(MC13.trident, r, "item.trident.return");
        ITEM_TRIDENT_THUNDER = register(MC13.trident, r, "item.trident.thunder");
        ITEM_TRIDENT_THROW = register(MC13.trident, r, "item.trident.throw");
        ITEM_TRIDENT_HIT_GROUND = register(MC13.trident, r, "item.trident.hit_ground");
        ITEM_TRIDENT_RIPTIDE_1 = register(MC13.trident, r, "item.trident.riptide_1");
        ITEM_TRIDENT_RIPTIDE_2 = register(MC13.trident, r, "item.trident.riptide_2");
        ITEM_TRIDENT_RIPTIDE_3 = register(MC13.trident, r, "item.trident.riptide_3");


        BARREL_OPEN = register(MC14.barrel, r, "block.barrel.open");
        BARREL_CLOSE = register(MC14.barrel, r, "block.barrel.close");

        UI_STONECUTTER_TAKE_RESULT = register(MC14.stonecutter, r, "ui.stonecutter.take_result");
    }

    private static SoundEvent register(Feature feature, IForgeRegistry<SoundEvent> registry, String name) {
        if (feature == null || !feature.isEnabled()) {
            return null;
        }
        SoundEvent event = new SoundEvent(new ResourceLocation(EFMTags.MODID, name));
        event.setRegistryName(event.getSoundName());
        registry.register(event);
        return event;
    }
}
