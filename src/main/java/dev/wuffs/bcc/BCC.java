package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BCC.MODID)
public class BCC {
    public static final String MODID = "bcc";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static Logger getLogger() {
        return LOGGER;
    }
    public static PingData localPingData = new PingData();

    public BCC() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonSetup);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    private void doCommonSetup(final FMLCommonSetupEvent event) {
        if(Config.useMetadata.get()) {
            Path metaFile = FMLPaths.CONFIGDIR.get().resolve("metadata.json");
            if(!Files.exists(metaFile)) {
                LOGGER.error("No metadata.json found, falling back to config values");
            }else {
                try {
                    LOGGER.info("Loading metadata.json");
                    Metadata metadata = new Gson().fromJson(Files.newBufferedReader(metaFile), Metadata.class);
                    localPingData.projectID = metadata.id;
                    localPingData.name = metadata.name;
                    localPingData.version = metadata.version.name;
                    localPingData.versionID = metadata.version.id;
                    localPingData.releaseType = metadata.version.type;
                    localPingData.isMetadata = true;
                    return;
                }catch(IOException e) {
                    LOGGER.error("Failed to read metadata.json", e);
                }
            }
        }
        localPingData.projectID = Config.modpackProjectID.get();
        localPingData.name = Config.modpackName.get();
        localPingData.version = Config.modpackVersion.get();
    }
}
