package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import dev.wuffs.bcc.data.BetterStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.Consumer;

public class BetterCompatibilityChecker {
    private static BetterCompatibilityChecker INSTANCE;
    public static final CrossPlatform PLATFORM = ServiceLoader.load(CrossPlatform.class).findFirst().orElseThrow();

    public static final String MOD_ID = "bcc";

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private static BetterStatus betterStatus = null;

    public static BetterCompatibilityChecker get() {
        if (INSTANCE == null) {
            INSTANCE = new BetterCompatibilityChecker();
        }

        return INSTANCE;
    }

    private BetterCompatibilityChecker() {
        LOGGER.info("Better Compatibility Checker starting");
    }

    public void init() {
        // Force load config
        Config.data();
    }

    public void onSetup(Consumer<BetterStatus> onSuccess) {
        LOGGER.info("Better Compatibility Checker setup");
        if (Config.data().useMetadata().value()) {
            Path metaFile = PLATFORM.configPath().resolve("metadata.json");
            if (!Files.exists(metaFile)) {
                LOGGER.error("No metadata.json found, falling back to config values");
            } else {
                try {
                    LOGGER.info("Loading metadata.json");
                    Metadata metadata = new Gson().fromJson(Files.newBufferedReader(metaFile), Metadata.class);
                    LOGGER.info("Loaded metadata.json - Modpack: {} | Version: {}", metadata.name, metadata.version.name);
                    BetterCompatibilityChecker.updateStatus(new BetterStatus(
                            metadata.name,
                            metadata.version.name,
                            true
                    ));
                    onSuccess.accept(betterStatus);
                    return;
                } catch (IOException e) {
                    LOGGER.error("Failed to read metadata.json", e);
                }
            }
        }

        var confData = Config.data();
        BetterCompatibilityChecker.updateStatus(new BetterStatus(
                confData.modpackName().value(),
                confData.modpackVersion().value(),
                false
        ));

        onSuccess.accept(betterStatus);
    }

    public static boolean comparePingData(BetterStatus otherStatus) {
        return otherStatus.name().equals(betterStatus.name()) && otherStatus.version().equals(betterStatus.version());
    }

    public static void updateStatus(BetterStatus newStatus) {
        betterStatus = newStatus;
    }

    public static @Nullable BetterStatus getBetterStatus() {
        return betterStatus;
    }
}
