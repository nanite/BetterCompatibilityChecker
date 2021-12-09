package dev.wuffs.bcc;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Config {
    static Path configFile = FabricLoader.getInstance().getConfigDir().resolve("/bcc.json");

    public static boolean create(){
        BCC.getLogger().warn("Config file not found, creating default config");
        try {
            Files.write(configFile, new Gson().toJson(new ModConfig()).getBytes(), StandardOpenOption.CREATE);
            return true;
        } catch (IOException e) {
            BCC.getLogger().error("Failed to create config file", e);
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
            BCC.getLogger().info("Loading config file");
            return new Gson().fromJson(Files.newBufferedReader(configFile), ModConfig.class);
        } catch (IOException e) {
            BCC.getLogger().error("Failed to read config file", e);
            return null;
        }
    }
}
