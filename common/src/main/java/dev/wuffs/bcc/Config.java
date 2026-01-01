package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Config {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ConfigData DEFAULT = new ConfigData(
            new StringEntry("CHANGE_ME", List.of("The name of the modpack")),
            new StringEntry("CHANGE_ME", List.of("The version of the modpack")),
            new BooleanEntry(false, List.of("Use the metadata.json to determine the modpack version", "ONLY ENABLE THIS IF YOU KNOW WHAT YOU ARE DOING"))
    );

    private static Config instance;

    private ConfigData data;
    private Path configPath;

    public static ConfigData data() {
        return get().data;
    }

    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public Config() {
        this.configPath = BetterCompatibilityChecker.PLATFORM.configPath().resolve("bcc-common.json");
        this.load();
    }

    private void load() {
        if (!Files.exists(configPath)) {
            this.writeDefault();
            return;
        }

        try {
            String jsonString = Files.readString(this.configPath);
            JsonElement jsonData = new Gson().fromJson(jsonString, JsonElement.class);
            this.data = ConfigData.CODEC.parse(JsonOps.INSTANCE, jsonData)
                    .getOrThrow();

        } catch (IOException e) {
            LOGGER.error("Failed to read config", e);
            this.data = DEFAULT;
        }
    }

    private void writeDefault() {
        this.data = DEFAULT;
        try {
            JsonElement jsonData = ConfigData.CODEC.encodeStart(JsonOps.INSTANCE, this.data)
                    .getOrThrow();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(this.configPath, gson.toJson(jsonData));
        } catch (IOException e) {
            LOGGER.error("Failed to write default config", e);
        }
    }

    public record ConfigData(StringEntry modpackName, StringEntry modpackVersion, BooleanEntry useMetadata) {
        public static final Codec<ConfigData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                StringEntry.CODEC.fieldOf("modpackName").forGetter(ConfigData::modpackName),
                StringEntry.CODEC.fieldOf("modpackVersion").forGetter(ConfigData::modpackVersion),
                BooleanEntry.CODEC.fieldOf("useMetadata").forGetter(ConfigData::useMetadata)
        ).apply(instance, ConfigData::new));
    }

    public record StringEntry(String value, List<String> comments) {
        public static final Codec<StringEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("value").forGetter(StringEntry::value),
                Codec.STRING.listOf().fieldOf("comments").forGetter(StringEntry::comments)
        ).apply(instance, StringEntry::new));
    }

    public record BooleanEntry(boolean value, List<String> comments) {
        public static final Codec<BooleanEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("value").forGetter(BooleanEntry::value),
                Codec.STRING.listOf().fieldOf("comments").forGetter(BooleanEntry::comments)
        ).apply(instance, BooleanEntry::new));
    }
}
