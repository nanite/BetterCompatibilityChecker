package dev.wuffs.bcc.fabric;

import dev.wuffs.bcc.BetterCompatibilityChecker;
import net.fabricmc.api.ModInitializer;

public class BetterCompatabilityCheckerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterCompatibilityChecker.get().init();
        BetterCompatibilityChecker.get().onSetup(status -> {});
    }
}
