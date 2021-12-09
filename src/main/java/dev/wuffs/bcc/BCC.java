package dev.wuffs.bcc;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BCC implements ModInitializer {
    public static final String MODID = "bcc";
    public static ModConfig config;

    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return LOGGER;
    }

    public static PingData localPingData = new PingData();

    public BCC() {

    }

	public static boolean comparePingData(PingData pingData) {
		return pingData.projectID == localPingData.projectID && pingData.name.equals(localPingData.name) && pingData.version.equals(localPingData.version);
	}

    @Override
    public void onInitialize() {
		config = Config.load();
		if (config != null) {
			BCC.getLogger().info("Config file loaded");
			if(config.useMetadata) {
				Path metaFile = FabricLoader.getInstance().getConfigDir().resolve("metadata.json");
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
			localPingData.projectID = config.projectID;
			localPingData.name = config.modpackName;
			localPingData.version = config.modpackVersion;
		}else{
			getLogger().error("Failed to load config, disabling mod");
		}
    }
}
