package com.serenibyss.etfuturum.sounds;

import com.serenibyss.etfuturum.EFMTags;
import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.serenibyss.etfuturum.load.feature.Features.*;

public class EFMSounds {

    private static final List<SoundEvent> ALL_SOUNDS = new ArrayList<>();

    public static final SoundEvent BARREL_OPEN = create(MC14.barrel, "block.barrel.open");
    public static final SoundEvent BARREL_CLOSE = create(MC14.barrel, "block.barrel.close");
    public static final SoundEvent UI_STONECUTTER_TAKE_RESULT = create(MC14.stonecutter, "ui.stonecutter.take_result");

    public static final SoundEvent ENTITY_PHANTOM_AMBIENT = create(MC13.phantom, "entity.phantom.ambient");
    public static final SoundEvent ENTITY_PHANTOM_BITE = create(MC13.phantom, "entity.phantom.bite");
    public static final SoundEvent ENTITY_PHANTOM_DEATH = create(MC13.phantom, "entity.phantom.death");
    public static final SoundEvent ENTITY_PHANTOM_FLAP = create(MC13.phantom, "entity.phantom.flap");
    public static final SoundEvent ENTITY_PHANTOM_HURT = create(MC13.phantom, "entity.phantom.hurt");
    public static final SoundEvent ENTITY_PHANTOM_SWOOP = create(MC13.phantom, "entity.phantom.swoop");

    public static final SoundEvent BLOCK_CONDUIT_AMBIENT = create(MC13.conduit, "block.conduit.ambient");
    public static final SoundEvent BLOCK_CONDUIT_AMBIENT_SHORT = create(MC13.conduit, "block.conduit.ambient.short");
    public static final SoundEvent BLOCK_CONDUIT_ATTACK_TARGET = create(MC13.conduit, "block.conduit.attack.target");
    public static final SoundEvent BLOCK_CONDUIT_ACTIVATE = create(MC13.conduit, "block.conduit.activate");
    public static final SoundEvent BLOCK_CONDUIT_DEACTIVATE = create(MC13.conduit, "block.conduit.deactivate");

    public static final SoundEvent ITEM_AXE_STRIP_LOG = create(MC13.stripping, "item.axe.strip");

    public static final SoundEvent ITEM_TRIDENT_HIT = create(MC13.trident, "item.trident.hit");
    public static final SoundEvent ITEM_TRIDENT_RETURN = create(MC13.trident, "item.trident.return");
    public static final SoundEvent ITEM_TRIDENT_THUNDER = create(MC13.trident, "item.trident.thunder");
    public static final SoundEvent ITEM_TRIDENT_THROW = create(MC13.trident, "item.trident.throw");
    public static final SoundEvent ITEM_TRIDENT_HIT_GROUND = create(MC13.trident, "item.trident.hit_ground");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_1 = create(MC13.trident, "item.trident.riptide_1");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_2 = create(MC13.trident, "item.trident.riptide_2");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_3 = create(MC13.trident, "item.trident.riptide_3");

    public static final SoundEvent ENTITY_TURTLE_AMBIENT_LAND = create(MC13.turtle, "entity.turtle.ambient_land");
    public static final SoundEvent ENTITY_TURTLE_SWIM = create(MC13.turtle, "entity.turtle.swim");
    public static final SoundEvent ENTITY_TURTLE_HURT = create(MC13.turtle, "entity.turtle.hurt");
    public static final SoundEvent ENTITY_TURTLE_HURT_BABY = create(MC13.turtle, "entity.turtle.hurt_baby");
    public static final SoundEvent ENTITY_TURTLE_DEATH = create(MC13.turtle, "entity.turtle.death");
    public static final SoundEvent ENTITY_TURTLE_DEATH_BABY = create(MC13.turtle, "entity.turtle.death_baby");
    public static final SoundEvent ENTITY_TURTLE_SHAMBLE = create(MC13.turtle, "entity.turtle.shamble");
    public static final SoundEvent ENTITY_TURTLE_SHAMBLE_BABY = create(MC13.turtle, "entity.turtle.shamble_baby");
    public static final SoundEvent ENTITY_TURTLE_EGG_CRACK = create(MC13.turtle, "entity.turtle.egg_crack");
    public static final SoundEvent ENTITY_TURTLE_EGG_HATCH = create(MC13.turtle, "entity.turtle.egg_hatch");
    public static final SoundEvent ENTITY_TURTLE_EGG_BREAK = create(MC13.turtle, "entity.turtle.egg_break");
    public static final SoundEvent ENTITY_TURTLE_LAY_EGG = create(MC13.turtle, "entity.turtle.lay_egg");
    public static final SoundEvent ITEM_ARMOR_EQUIP_TURTLE = create(MC13.turtle, "item.armor.equip_turtle");

    public static final SoundEvent ITEM_BUCKET_FILL_FISH = create(MC13.fish, "item.bucket.fill_fish");
    public static final SoundEvent ENTITY_FISH_SWIM = create(MC13.fish, "entity.fish.swim");
    public static final SoundEvent ENTITY_COD_AMBIENT = create(MC13.fish, "entity.cod.ambient");
    public static final SoundEvent ENTITY_COD_DEATH = create(MC13.fish, "entity.cod.death");
    public static final SoundEvent ENTITY_COD_HURT = create(MC13.fish, "entity.cod.hurt");
    public static final SoundEvent ENTITY_COD_FLOP = create(MC13.fish, "entity.cod.flop");
    public static final SoundEvent ENTITY_SALMON_AMBIENT = create(MC13.fish, "entity.salmon.ambient");
    public static final SoundEvent ENTITY_SALMON_DEATH = create(MC13.fish, "entity.salmon.death");
    public static final SoundEvent ENTITY_SALMON_HURT = create(MC13.fish, "entity.salmon.hurt");
    public static final SoundEvent ENTITY_SALMON_FLOP = create(MC13.fish, "entity.salmon.flop");
    public static final SoundEvent ENTITY_PUFFER_FISH_AMBIENT = create(MC13.fish, "entity.puffer_fish.ambient");
    public static final SoundEvent ENTITY_PUFFER_FISH_BLOW_UP = create(MC13.fish, "entity.puffer_fish.blow_up");
    public static final SoundEvent ENTITY_PUFFER_FISH_BLOW_OUT = create(MC13.fish, "entity.puffer_fish.blow_out");
    public static final SoundEvent ENTITY_PUFFER_FISH_DEATH = create(MC13.fish, "entity.puffer_fish.death");
    public static final SoundEvent ENTITY_PUFFER_FISH_HURT = create(MC13.fish, "entity.puffer_fish.hurt");
    public static final SoundEvent ENTITY_PUFFER_FISH_FLOP = create(MC13.fish, "entity.puffer_fish.flop");
    public static final SoundEvent ENTITY_PUFFER_FISH_STING = create(MC13.fish, "entity.puffer_fish.sting");
    public static final SoundEvent ENTITY_TROPICAL_FISH_AMBIENT = create(MC13.fish, "entity.tropical_fish.ambient");
    public static final SoundEvent ENTITY_TROPICAL_FISH_DEATH = create(MC13.fish, "entity.tropical_fish.death");
    public static final SoundEvent ENTITY_TROPICAL_FISH_HURT = create(MC13.fish, "entity.tropical_fish.hurt");
    public static final SoundEvent ENTITY_TROPICAL_FISH_FLOP = create(MC13.fish, "entity.tropical_fish.flop");

    private static SoundEvent create(Feature feature, String name) {
        if (feature == null || !feature.isEnabled()) {
            return null;
        }
        SoundEvent event = new SoundEvent(new ResourceLocation(EFMTags.MODID, name));
        ALL_SOUNDS.add(event);
        return event;
    }

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        final IForgeRegistry<SoundEvent> r = event.getRegistry();

        for (SoundEvent sound : ALL_SOUNDS) {
            sound.setRegistryName(sound.getSoundName());
            r.register(sound);
        }
        ALL_SOUNDS.clear();
    }
}
