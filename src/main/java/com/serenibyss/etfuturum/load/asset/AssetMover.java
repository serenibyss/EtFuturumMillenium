package com.serenibyss.etfuturum.load.asset;

import com.serenibyss.etfuturum.load.feature.MCVersion;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class AssetMover {

    static final Logger LOGGER = LogManager.getLogger("EtFuturum Asset Mover");
    static final Path PARENT_PATH = Paths.get("").resolve("etfuturumassets");

    static final List<AssetRequest> REQUESTS = new ArrayList<>();
    static ProgressManager.ProgressBar PROGRESS_BAR;

    public static void queue(AssetRequest request) {
        if (!request.isEmpty()) {
            REQUESTS.add(request);
        }
    }

    public static void flush() {
        if (REQUESTS.size() == 0) return;
        validateLoadingState();

        Map<MCVersion, Map<String, String>> requestsToSend = new Object2ObjectOpenHashMap<>();
        for (AssetRequest request : REQUESTS) {
            Map<String, String> requestMap = request.getAssetRequest();
            if (isUpdated(requestMap)) {
                continue;
            }
            requestsToSend.put(request.getMinecraftVersion(), requestMap);
        }
        REQUESTS.clear();

        if (requestsToSend.size() > 0) {
            PROGRESS_BAR = ProgressManager.push("Et Futurum Asset Mover", requestsToSend.size());
            for (var entry : requestsToSend.entrySet()) {
                move(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void move(MCVersion mcVersion, Map<String, String> assets) {
        assets = new Object2ObjectOpenHashMap<>(assets);
        if (isUpdated(assets)) {
            return;
        }
        try {
            AssetHelper.fromMinecraftVersion(mcVersion, assets);
        } catch (SSLHandshakeException e) {
            LOGGER.fatal("Unexpected error occurred, perhaps update your Java version?", e);
        } catch (IOException e) {
            LOGGER.fatal("Unexpected error occurred", e);
        }
    }

    private static void validateLoadingState() {
        if (Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION)) {
            throw new RuntimeException("Asset moving operations can only be performed during FMLConstructionEvent or earlier!");
        }
    }

    private static boolean isUpdated(Map<String, String> assets) {
        boolean allUpdated = true;
        Iterator<Map.Entry<String, String>> iter = assets.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            if (Files.exists(PARENT_PATH.resolve(entry.getValue()))) {
                iter.remove();
            } else {
                allUpdated = false;
            }
        }
        return allUpdated;
    }
}
