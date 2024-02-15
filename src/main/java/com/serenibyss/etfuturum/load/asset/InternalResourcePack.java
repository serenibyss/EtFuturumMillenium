package com.serenibyss.etfuturum.load.asset;

import com.google.gson.JsonObject;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLContainerHolder;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class InternalResourcePack extends FolderResourcePack implements FMLContainerHolder {

    private final ModContainer mc;

    public InternalResourcePack(ModContainer mc) {
        super(new File(Launch.minecraftHome, "etfuturumassets"));
        this.mc = mc;
    }

    @Override
    public ModContainer getFMLContainer() {
        return mc;
    }

    @NotNull
    @Override
    public String getPackName() {
        return "EFMAssetPack";
    }

    @NotNull
    @Override
    public Set<String> getResourceDomains() {
        AssetHelper.haltAndFlush();
        return super.getResourceDomains();
    }

    @NotNull
    @Override
    public <T extends IMetadataSection> T getPackMetadata(@NotNull MetadataSerializer metadataSerializer, @NotNull String metadataSectionName) {
        JsonObject metadata = new JsonObject();
        JsonObject packObj = new JsonObject();
        metadata.add("pack", packObj);
        packObj.addProperty("description", "Includes assets moved by Et Futurum Millenium.");
        packObj.addProperty("pack_format", 2);
        return metadataSerializer.parseMetadataSection(metadataSectionName, metadata);
    }
}
