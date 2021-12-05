package dev.wuffs.modpacktagger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Mod(ModpackTagger.MODID)
public class ModpackTagger {
    public static final String MODID = "modpacktagger";
    private static final Logger LOGGER = LogManager.getLogger();
    public static JsonElement metaData;
    private static final String FMLConfigFolder = "config";

    public ModpackTagger() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLDedicatedServerSetupEvent event) -> {
            try {
                doServerStuff(event);
            } catch (IOException ignored) {
            }
        });
    }

    void doServerStuff(final FMLDedicatedServerSetupEvent event) throws IOException {
        File metaFile = new File(FMLConfigFolder, "metadata.json");
        if (!metaFile.exists()) {
            LOGGER.info("No metadata.json found, skipping modpacktagger");
        } else {
            FileReader serversFile = new FileReader(metaFile.getPath());
            JsonReader reader = new JsonReader(serversFile);
            metaData = new JsonParser().parse(reader);
        }
    }
}
