package com.serenibyss.etfuturum.load;

import com.google.common.collect.Sets;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class EtFuturumConfigLoader {

    private static final Logger LOGGER = LogManager.getLogger("EtFuturum Config Loader");

    private static final MethodHandle CONFIGMANAGER_SYNC;

    static {
        try {
            Class.forName("net.minecraftforge.common.config.ConfigManager", true, Launch.classLoader);
            Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            implLookup.setAccessible(true);
            Lookup lookup = ((Lookup) implLookup.get(null)).in(ConfigManager.class);
            CONFIGMANAGER_SYNC = lookup.findStatic(ConfigManager.class, "sync", MethodType.methodType(void.class, Configuration.class, Class.class, String.class, String.class, boolean.class, Object.class));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiStatus.Internal
    public static void init() {}

    public static void register(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Config.class)) {
            LOGGER.warn("Tried to load config class '{}' but it is not annotated with @Config. Skipping...", configClass.getCanonicalName());
            return;
        }
        try {
            Method findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            findLoadedClass.setAccessible(true);
            if (findLoadedClass.invoke(Launch.classLoader, "net.minecraftforge.fml.common.Loader") != null) {
                if (!Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION)) {
                    $register(configClass); // early
                }
            } else {
                $register(configClass); // early
            }
            // late, no action needed
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("all")
    private static void $register(Class<?> configClass) throws Throwable {
        LOGGER.info("Registering config class '{}' for early loading", configClass.getCanonicalName());
        Field modConfigClasses = ConfigManager.class.getDeclaredField("MOD_CONFIG_CLASSES");
        Field configs = ConfigManager.class.getDeclaredField("CONFIGS");
        modConfigClasses.setAccessible(true);
        configs.setAccessible(true);

        Map<String, Set<Class<?>>> MOD_CONFIG_CLASSES = (Map<String, Set<Class<?>>>) modConfigClasses.get(null);
        Map<String, Configuration> CONFIGS = (Map<String, Configuration>) configs.get(null);

        Config config = configClass.getAnnotation(Config.class);
        String modId = config.modid();

        Set<Class<?>> classes = MOD_CONFIG_CLASSES.computeIfAbsent(modId, k -> Sets.newHashSet());
        classes.add(configClass);

        File configDir = new File(Launch.minecraftHome, "config");
        File configFile = new File(configDir, config.name() + ".cfg");
        Configuration cfg = CONFIGS.get(configFile.getAbsolutePath());
        if (cfg == null) {
            cfg = new Configuration(configFile);
            cfg.load();
            CONFIGS.put(configFile.getAbsolutePath(), cfg);
        }

        CONFIGMANAGER_SYNC.invokeExact(cfg, configClass, modId, config.category(), true, (Object) null);

        cfg.save();
    }
}
