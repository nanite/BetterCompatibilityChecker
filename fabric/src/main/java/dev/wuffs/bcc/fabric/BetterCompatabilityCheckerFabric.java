package dev.wuffs.bcc.fabric;

import dev.wuffs.bcc.BetterCompatibilityChecker;
import dev.wuffs.bcc.Config;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class BetterCompatabilityCheckerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigRegistry.INSTANCE.register(BetterCompatibilityChecker.MOD_ID, ModConfig.Type.COMMON, Config.CONFIG);

        BetterCompatibilityChecker.get().init();
        BetterCompatibilityChecker.get().onSetup(status -> {});
    }
}
