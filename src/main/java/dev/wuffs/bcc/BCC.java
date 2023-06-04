package dev.wuffs.bcc;

import com.google.gson.Gson;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(BCC.MODID)
public class BCC {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "bcc";

    public BCC() {
        LOGGER.info("Better Compatibility Checker loading");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonSetup);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    private void doCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Better Compatibility Checker setting up");
        if(Config.useMetadata.get()) {
            Path metaFile = FMLPaths.CONFIGDIR.get().resolve("metadata.json");
            if(!Files.exists(metaFile)) {
                LOGGER.error("No metadata.json found, falling back to config values");
            }else {
                try {
                    LOGGER.info("Loading metadata.json");
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
                    LOGGER.error("Failed to read metadata.json", e);
                }
            }
        }

        BetterStatusServerHolder.INSTANCE.setStatus(new BetterStatus(
                Config.modpackProjectID.get(),
                Config.modpackName.get(),
                Config.modpackVersion.get(),
                -1,
                "unknown",
                false
        ));
    }

    public static boolean comparePingData(BetterStatus pingData) {
        BetterStatus status = BetterStatusServerHolder.INSTANCE.getStatus();
        return pingData.projectId() == status.projectId() && pingData.name().equals(status.name()) && pingData.version().equals(status.version());
    }
}
