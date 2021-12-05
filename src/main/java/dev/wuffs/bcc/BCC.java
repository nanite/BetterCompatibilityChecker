package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    private static final String FMLConfigFolder = "config";

    public BCC() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> {
            try {
                doCommonSetup(event);
            } catch (IOException ignored) {
            }
        });
    }

    private void doCommonSetup(final FMLCommonSetupEvent event) throws IOException {
        if (Config.useMetadata.get()) {
            File metaFile = new File(FMLConfigFolder, "metadata.json");
            if (!metaFile.exists()) {
                LOGGER.error("No metadata.json found, falling back to config values");
                localPingData.projectID = Config.modpackProjectID.get();
                localPingData.name = Config.modpackName.get();
                localPingData.version = Config.modpackVersion.get();
            } else {
                FileReader file = new FileReader(metaFile.getPath());
                JsonReader reader = new JsonReader(file);
                Metadata metadata = new Gson().fromJson(reader, Metadata.class);
                localPingData.projectID = metadata.id;
                localPingData.name = metadata.name;
                localPingData.version = metadata.version.name;
                localPingData.versionID = metadata.version.id;
                localPingData.releaseType = metadata.version.type;
                localPingData.isMetadata = true;
            }
        }else{
            localPingData.projectID = Config.modpackProjectID.get();
            localPingData.name = Config.modpackName.get();
            localPingData.version = Config.modpackVersion.get();
        }
    }
}
