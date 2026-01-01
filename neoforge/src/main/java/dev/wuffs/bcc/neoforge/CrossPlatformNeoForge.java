package dev.wuffs.bcc.neoforge;

import dev.wuffs.bcc.CrossPlatform;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class CrossPlatformNeoForge implements CrossPlatform {
    @Override
    public Path configPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
