package com.serenibyss.etfuturum;

import com.serenibyss.etfuturum.load.CommonProxy;
import com.serenibyss.etfuturum.load.feature.Features;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = EFMTags.MODID,
     version = EFMTags.VERSION,
     name = EFMTags.MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required-after:mixinbooter;required-after:assetmover;required-after:configanytime;")
public class EtFuturum {

    public static final Logger LOGGER = LogManager.getLogger(EFMTags.MODID);

    @Instance
    public static EtFuturum INSTANCE;

    @SidedProxy(clientSide = "com.serenibyss.etfuturum.load.ClientProxy", serverSide = "com.serenibyss.etfuturum.load.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        if (event.getSide() == Side.CLIENT) {
            Features.initAssets();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
