package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
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
    public static PingData localPingData = new PingData();
    public static PingData remotePingData = new PingData();
    public static JsonElement metaData;
    private static final String FMLConfigFolder = "config";

    public BCC() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> {
            try {
                doClientThings(event);
            } catch (IOException ignored) {
            }
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLDedicatedServerSetupEvent event) -> {
            try {
                doServerStuff(event);
            } catch (IOException ignored) {
            }
        });
    }

    private void doClientThings(final FMLClientSetupEvent event) throws IOException{
        if (Config.useManifest.get()) {
            File metaFile = new File(FMLConfigFolder, "metadata.json");
            if (!metaFile.exists()) {
                LOGGER.info("No metadata.json found, skipping modpacktagger");
            } else {
                FileReader file = new FileReader(metaFile.getPath());
                JsonReader reader = new JsonReader(file);
                localPingData = new Gson().fromJson(reader, PingData.Manifest.class);
            }
        }else {
            localPingData.projectID = Config.modpackProjectID.get();
            localPingData.name = Config.modpackName.get();
            localPingData.version = Config.modpackVersion.get();
        }
    }

    private void doServerStuff(final FMLDedicatedServerSetupEvent event) throws IOException {
        if (Config.useManifest.get()) {
            File metaFile = new File(FMLConfigFolder, "metadata.json");
            if (!metaFile.exists()) {
                LOGGER.info("No metadata.json found, skipping modpacktagger");
            } else {
                FileReader serversFile = new FileReader(metaFile.getPath());
                JsonReader reader = new JsonReader(serversFile);
                metaData = JsonParser.parseReader(reader);
            }
        }
    }
}
