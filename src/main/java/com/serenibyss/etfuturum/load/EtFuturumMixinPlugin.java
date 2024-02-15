package com.serenibyss.etfuturum.load;

import com.google.common.collect.ImmutableMap;
import com.serenibyss.etfuturum.load.feature.Feature;
import com.serenibyss.etfuturum.load.feature.FeatureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class EtFuturumMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LogManager.getLogger("EtFuturum Mixin Loader");

    private static final String PACKAGE_PREFIX = "com.serenibyss.etfuturum.mixin.";

    private static final ImmutableMap<String, Consumer<PotentialMixin>> MIXIN_PROCESSING_MAP = ImmutableMap.<String, Consumer<PotentialMixin>>builder()
            .put("Lorg/spongepowered/asm/mixin/Mixin;", p -> p.valid = true)
            .put("Lcom/serenibyss/etfuturum/load/annotation/ClientMixin;", p -> p.isClientOnly = true)
            .put("Lcom/serenibyss/etfuturum/load/annotation/LateMixin;", p -> p.isLate = true)
            .build();

    static class PotentialMixin {
        String className;
        boolean valid;
        boolean isClientOnly;
        boolean isLate;
    }

    private static final List<PotentialMixin> allMixins = new ArrayList<>();

    private void considerClass(String pathString) throws IOException {
        try (InputStream stream = EtFuturumMixinPlugin.class.getClassLoader().getResourceAsStream("com/serenibyss/etfuturum/mixin/" + pathString)) {
            if (stream == null) {
                return;
            }
            ClassReader reader = new ClassReader(stream);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            if (node.invisibleAnnotations == null) {
                return;
            }
            PotentialMixin mixin = new PotentialMixin();
            mixin.className = node.name.replace('/', '.');
            for (AnnotationNode annotation : node.invisibleAnnotations) {
                Consumer<PotentialMixin> consumer = MIXIN_PROCESSING_MAP.get(annotation.desc);
                if (consumer != null) {
                    consumer.accept(mixin);
                }
            }
            if (mixin.valid) {
                allMixins.add(mixin);
            }
        }
    }

    private static String mixinClassToPackage(String mixinClassName) {
        String noPrefix = mixinClassName.replace(PACKAGE_PREFIX, "");
        return noPrefix.substring(0, noPrefix.indexOf('.'));
    }

    @Override
    public void onLoad(String s) {
        if (allMixins.size() == 0) {
            try {
                URI uri = Objects.requireNonNull(EtFuturumMixinPlugin.class.getResource("/mixins.etfuturum.json")).toURI();
                FileSystem fs;
                try {
                    fs = FileSystems.getFileSystem(uri);
                } catch (FileSystemNotFoundException var11) {
                    fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
                }
                List<Path> list;
                Path basePath = fs.getPath("com", "serenibyss", "etfuturum", "mixin").toAbsolutePath();
                try (Stream<Path> stream = Files.walk(basePath)) {
                    list = stream.collect(Collectors.toList());
                }
                for (Path p : list) {
                    if (p == null) {
                        continue;
                    }
                    p = basePath.relativize(p.toAbsolutePath());
                    String pathString = p.toString();
                    if (pathString.endsWith(".class")) {
                        considerClass(pathString);
                    }
                }
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Found {} mixins", allMixins.size());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetName, String className) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    public static boolean isMixinClassApplied(String name) {
        String mixinPackage = mixinClassToPackage(name);
        Feature feature = FeatureManager.getFeatureByName(mixinPackage);
        if (feature == null) {
            LOGGER.warn("Not applying mixin '{}' as it is not associated with any feature", name);
            return false;
        }
        if (!feature.isEnabled()) {
            LOGGER.info("Skipping mixin '{}' as feature '{}' is disabled", name, feature);
            return false;
        }
        LOGGER.info("Loading mixin '{}'", name);
        return true;
    }

    @Override
    public List<String> getMixins() {
        MixinEnvironment.Phase phase = MixinEnvironment.getCurrentEnvironment().getPhase();
        if (phase == MixinEnvironment.Phase.DEFAULT) {
            MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment().getSide();
            List<String> list = allMixins.stream()
                    .filter(p -> !p.isClientOnly || side == MixinEnvironment.Side.CLIENT)
                    .filter(p -> p.isLate == EtFuturumLateMixinLoader.atLateStage)
                    .map(p -> p.className)
                    .filter(EtFuturumMixinPlugin::isMixinClassApplied)
                    .map(clz -> clz.replace("com.serenibyss.etfuturum.mixin.", ""))
                    .collect(Collectors.toList());
            for (String mixin : list) {
                LOGGER.debug("loading {}", mixin);
            }
            return list;
        }
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
