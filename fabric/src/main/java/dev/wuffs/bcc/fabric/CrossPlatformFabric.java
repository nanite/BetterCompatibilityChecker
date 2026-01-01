package dev.wuffs.bcc.fabric;

import dev.wuffs.bcc.CrossPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class CrossPlatformFabric implements CrossPlatform {
    @Override
    public Path configPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
