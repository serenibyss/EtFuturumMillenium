package com.serenibyss.etfuturum.load.asset.pack;

import com.google.common.collect.ImmutableSet;
import com.serenibyss.etfuturum.EFMTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SideOnly(Side.CLIENT)
public abstract class LangOverridePack extends AbstractResourcePack {

    private static final String PACK_ID = "vanilla_overrides";
    public static final Set<String> langOverrides = new HashSet<>();

    public LangOverridePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    public static void init() {
        File file = Loader.instance().activeModContainer().getSource();
        LangOverridePack pack;
        if (file.isDirectory()) {
            pack = new LangFolderResourcePack(file);
        } else {
            pack = new LangFileResourcePack(file);
        }
        List<IResourcePack> packs = ObfuscationReflectionHelper.getPrivateValue(
                Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao");
        packs.add(pack);
        if (Minecraft.getMinecraft().getResourceManager() instanceof IReloadableResourceManager m) {
            m.reloadResources(packs);
        }
    }

    @Override
    public @NotNull String getPackName() {
        return EFMTags.MODID + "/" + PACK_ID;
    }

    @Override
    public BufferedImage getPackImage() {
        return null;
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(@NotNull MetadataSerializer serializer, @NotNull String name) {
        return null;
    }

    protected String getRootPath() {
        return "resourcepacks/" + PACK_ID + "/";
    }

    @SideOnly(Side.CLIENT)
    private static class LangFileResourcePack extends LangOverridePack {

        private final ZipFile zipFile;

        public LangFileResourcePack(File file) {
            super(file);
            try {
                this.zipFile = new ZipFile(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public @NotNull Set<String> getResourceDomains() {
            return ImmutableSet.of("minecraft");
        }

        @Override
        protected @NotNull InputStream getInputStreamByName(@NotNull String name) throws IOException {
            ZipEntry entry = zipFile.getEntry(getRootPath() + name);
            if (name.endsWith("lang")) {
                InputStream stream = zipFile.getInputStream(entry);
                Scanner scanner = new Scanner(stream);
                List<String> langFile = new ArrayList<>();

                // Remove unneeded lines
                String currentLine;
                while (scanner.hasNextLine()) {
                    currentLine = scanner.nextLine();
                    if (currentLine.startsWith("#") || currentLine.length() == 0) {
                        continue;
                    }
                    langFile.add(currentLine.trim());
                }

                Iterator<String> iterator = langFile.listIterator();
                while (iterator.hasNext()) {
                    String translation = iterator.next();
                    for (String removalCheck : langOverrides) {
                        if (translation.startsWith(removalCheck)) {
                            iterator.remove();
                        }
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (String line : langFile) {
                    baos.write((line + "\n").getBytes());
                }

                byte[] bytes = baos.toByteArray();
                return new ByteArrayInputStream(bytes);
            }
            return zipFile.getInputStream(entry);
        }

        @Override
        protected boolean hasResourceName(@NotNull String name) {
            return name.endsWith("lang") && zipFile.getEntry(getRootPath() + name) != null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static class LangFolderResourcePack extends LangOverridePack {

        public LangFolderResourcePack(File resourcePackFileIn) {
            super(resourcePackFileIn);
        }

        @Override
        public @NotNull Set<String> getResourceDomains() {
            return ImmutableSet.of("minecraft");
        }

        @Override
        protected @NotNull InputStream getInputStreamByName(@NotNull String name) throws IOException {
            if (name.endsWith("lang")) {
                List<String> langFile = new ArrayList<>();

                // Remove unneeded lines
                String currentLine;
                File file = new File(this.resourcePackFile, getRootPath() + "/" + name);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.startsWith("#") || currentLine.length() == 0) {
                        continue;
                    }
                    langFile.add(currentLine.trim());
                }

                Iterator<String> iterator = langFile.listIterator();
                while (iterator.hasNext()) {
                    String translation = iterator.next();
                    for (String removalCheck : langOverrides) {
                        if (translation.startsWith(removalCheck)) {
                            iterator.remove();
                        }
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (String line : langFile) {
                    baos.write((line + "\n").getBytes());
                }

                byte[] bytes = baos.toByteArray();
                return new ByteArrayInputStream(bytes);
            }
            return new BufferedInputStream(Files.newInputStream(new File(this.resourcePackFile, getRootPath() + "/" + name).toPath()));
        }

        @Override
        protected boolean hasResourceName(@NotNull String name) {
            return name.endsWith("lang") && new File(this.resourcePackFile, getRootPath() + "/" + name).isFile();
        }
    }
}
