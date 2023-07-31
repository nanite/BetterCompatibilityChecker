package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Config {
    static Path configFile = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".json");

    public static boolean create(){
        Constants.LOG.warn("Config file not found, creating default config");
        try {
            Files.write(configFile, new GsonBuilder().setPrettyPrinting().create().toJson(new ModConfig()).getBytes(), StandardOpenOption.CREATE);
            return true;
        } catch (IOException e) {
            Constants.LOG.error("Failed to create config file", e);
            return false;
        }
    }
    public static ModConfig load(){
        if (!Files.exists(configFile)) {
            boolean didCreate = create();
            if (!didCreate)
                return null;
        }
        try {
            Constants.LOG.info("Loading config file");
            return new Gson().fromJson(Files.newBufferedReader(configFile), ModConfig.class);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file", e);
            return null;
        }
    }
}
