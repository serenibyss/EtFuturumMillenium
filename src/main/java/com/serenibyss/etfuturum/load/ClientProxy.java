package com.serenibyss.etfuturum.load;

import com.serenibyss.etfuturum.client.particle.EFMParticleHandler;
import com.serenibyss.etfuturum.client.render.tile.TileEntityConduitRenderer;
import com.serenibyss.etfuturum.entities.EFMEntities;
import com.serenibyss.etfuturum.event.ClientEventHandler;
import com.serenibyss.etfuturum.load.asset.pack.BuiltInResourcePack;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.tiles.TileEntityConduit;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        BuiltInResourcePack.init();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void registerItemRendererWithOverride(Block block, Map<IProperty<?>, Comparable<?>> stateOverrides) {
        for (IBlockState state : block.getBlockState().getValidStates()) {
            Map<IProperty<?>, Comparable<?>> properties = new Object2ObjectOpenHashMap<>(state.getProperties());
            properties.putAll(stateOverrides);
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
                    block.getMetaFromState(state),
                    new ModelResourceLocation(block.getRegistryName(), statePropertiesToString(properties)));
        }
    }

    private static String statePropertiesToString(Map<IProperty<?>, Comparable<?>> properties) {
        StringBuilder stringbuilder = new StringBuilder();

        List<Map.Entry<IProperty<?>, Comparable<?>>> entries = properties.entrySet().stream()
                .sorted(Comparator.comparing(c -> c.getKey().getName()))
                .collect(Collectors.toList());

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : entries) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> property = entry.getKey();
            stringbuilder.append(property.getName());
            stringbuilder.append("=");
            //stringbuilder.append(property.getName(entry.getValue()));
        }

        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
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
