package dev.wuffs.bcc.platform;

import dev.wuffs.bcc.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Neo";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
}