package com.serenibyss.etfuturum.load.asset;

import com.google.common.eventbus.EventBus;
import com.serenibyss.etfuturum.EFMTags;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class AssetDummyMod extends DummyModContainer {

    public AssetDummyMod() {
        super(new ModMetadata());
        ModMetadata metadata = this.getMetadata();
        metadata.modId = "etfuturumassets";
        metadata.name = "Et Futurum Millenium Assets";
        metadata.description = "Allows acquiring of vanilla/mod assets at runtime without potentially violating licenses.";
        metadata.version = EFMTags.VERSION;
        metadata.authorList = Collections.singletonList("serenibyss");
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return InternalResourcePack.class;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
