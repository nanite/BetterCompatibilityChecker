package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import dev.wuffs.bcc.data.BetterStatus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.CrashReportCallables;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(BetterCompatibilityChecker.MOD_ID)
public class BetterCompatibilityChecker {
    public static final String MOD_ID = "bcc";

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private static BetterStatus betterStatus = null;

    public BetterCompatibilityChecker(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Better Compatibility Checker starting");
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Better Compatibility Checker setup");
        if (Config.useMetadata.get()) {
            Path metaFile = FMLPaths.CONFIGDIR.get().resolve("metadata.json");
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
                    registerCrashCallable();
                    return;
                } catch (IOException e) {
                    LOGGER.error("Failed to read metadata.json", e);
                }
            }
        }

        BetterCompatibilityChecker.updateStatus(new BetterStatus(
                Config.modpackName.get(),
                Config.modpackVersion.get(),
                false
        ));

        registerCrashCallable();
    }

    public static boolean comparePingData(BetterStatus otherStatus) {
        return otherStatus.name().equals(betterStatus.name()) && otherStatus.version().equals(betterStatus.version());
    }

    private static void registerCrashCallable() {
        LOGGER.info("Loaded BetterCompatibilityChecker - Modpack: {} | Version: {}", betterStatus.name(), betterStatus.version());
        CrashReportCallables.registerCrashCallable("BetterCompatibilityChecker", () -> "Modpack Name: " + betterStatus.name() + " | Modpack Version: " + betterStatus.version());
    }

    public static void updateStatus(BetterStatus newStatus) {
        betterStatus = newStatus;
    }

    public static @Nullable BetterStatus getBetterStatus() {
        return betterStatus;
    }
}
