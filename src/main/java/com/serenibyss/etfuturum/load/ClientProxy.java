package com.serenibyss.etfuturum.load;

import com.serenibyss.etfuturum.client.particle.EFMParticleHandler;
import com.serenibyss.etfuturum.client.render.tile.TileEntityConduitRenderer;
import com.serenibyss.etfuturum.entities.EFMEntities;
import com.serenibyss.etfuturum.event.ClientEventHandler;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.serenibyss.etfuturum.load.feature.Features.*;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ClientProxy.class);
        MinecraftForge.EVENT_BUS.register(EFMParticleHandler.class);

        EFMEntities.registerRenders();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public boolean isClientSide() {
        return true;
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(ModelRegistryEvent event) {
        registerSpecialRenderer(MC13.conduit, TileEntityConduit.class, new TileEntityConduitRenderer());
    }

    public static void registerCustomItemModels(List<String> m) {
        registerCustomItemModel(MC13.trident, m, "item/trident_in_hand");
    }

    private static <T extends TileEntity> void registerSpecialRenderer(Feature feature, Class<T> tileClass,
                                                                       TileEntitySpecialRenderer<? super T> renderer) {
        if (feature.isEnabled()) {
            ClientRegistry.bindTileEntitySpecialRenderer(tileClass, renderer);
        }
    }

    private static void registerCustomItemModel(Feature feature, List<String> customModels, String modelName) {
        if (feature.isEnabled()) {
            customModels.add(modelName);
        }
    }
}
