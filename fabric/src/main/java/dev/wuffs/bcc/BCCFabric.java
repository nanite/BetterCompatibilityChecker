package dev.wuffs.bcc;

import com.google.gson.Gson;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BCCFabric implements ModInitializer {

    public static ModConfig config;
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Better Compatibility Checker starting");
        config = Config.load();
        if (config != null) {
            Constants.LOG.info("Config file loaded");
            if(config.useMetadata) {
                Path metaFile = FabricLoader.getInstance().getConfigDir().resolve("metadata.json");
                if(!Files.exists(metaFile)) {
                    Constants.LOG.error("No metadata.json found, falling back to config values");
                }else {
                    try {
                        Constants.LOG.info("Loading metadata.json");
                        Metadata metadata = new Gson().fromJson(Files.newBufferedReader(metaFile), Metadata.class);
                        BetterStatusServerHolder.INSTANCE.setStatus(new BetterStatus(
                                metadata.id,
                                metadata.name,
                                metadata.version.name,
                                metadata.version.id,
                                metadata.version.type,
                                true
                        ));
                        return;
                    }catch(IOException e) {
                        Constants.LOG.error("Failed to read metadata.json", e);
                    }
                }
            }
            BetterStatusServerHolder.INSTANCE.setStatus(new BetterStatus(
                    config.projectID,
                    config.modpackName,
                    config.modpackVersion,
                    -1,
                    "unknown",
                    false
            ));
        }else{
            Constants.LOG.error("Failed to load config, disabling mod");
        }
    }
}
