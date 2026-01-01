package dev.wuffs.bcc.neoforge;

import com.mojang.logging.LogUtils;
import dev.wuffs.bcc.BetterCompatibilityChecker;
import dev.wuffs.bcc.data.BetterStatus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.CrashReportCallables;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(BetterCompatibilityChecker.MOD_ID)
public class BetterCompatibilityCheckerNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public BetterCompatibilityCheckerNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        BetterCompatibilityChecker.get().init();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BetterCompatibilityChecker.get().onSetup(BetterCompatibilityCheckerNeoForge::registerCrashCallable);
    }

    private static void registerCrashCallable(BetterStatus status) {
        LOGGER.info("Loaded BetterCompatibilityChecker - Modpack: {} | Version: {}", status.name(), status.version());
        CrashReportCallables.registerCrashCallable("BetterCompatibilityChecker", () -> "Modpack Name: " + status.name() + " | Modpack Version: " + status.version());
    }
}
