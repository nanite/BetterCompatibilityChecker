package dev.wuffs.bcc;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(BetterCompatibilityChecker.MODID)
public class BetterCompatibilityChecker
{
    public static final String MODID = "bcc";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BetterCompatibilityChecker(IEventBus modEventBus, ModContainer modContainer)
    {
        LOGGER.info("Better Compatibility Checker starting");
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Better Compatibility Checker setup");
        if(Config.useMetadata.get()) {
            Path metaFile = FMLPaths.CONFIGDIR.get().resolve("metadata.json");
            if(!Files.exists(metaFile)) {
                LOGGER.error("No metadata.json found, falling back to config values");
            }else {
                try {
                    LOGGER.info("Loading metadata.json");
                    Metadata metadata = new Gson().fromJson(Files.newBufferedReader(metaFile), Metadata.class);
                    BetterStatusServerHolder.INSTANCE.setStatus(new BetterStatus(
                            metadata.name,
                            metadata.version.name,
                            true
                    ));
                    return;
                }catch(IOException e) {
                    LOGGER.error("Failed to read metadata.json", e);
                }
            }
        }

        BetterStatusServerHolder.INSTANCE.setStatus(new BetterStatus(
                Config.modpackName.get(),
                Config.modpackVersion.get(),
                false
        ));
    }

    public static boolean comparePingData(BetterStatus pingData) {
        BetterStatus status = BetterStatusServerHolder.INSTANCE.getStatus();
        return pingData.name().equals(status.name()) && pingData.version().equals(status.version());
    }

}
