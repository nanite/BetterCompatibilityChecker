package dev.wuffs.bcc;

import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import dev.wuffs.bcc.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;


public class CommonClass {
    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));
    }
    public static boolean comparePingData(BetterStatus pingData) {
        BetterStatus status = BetterStatusServerHolder.INSTANCE.getStatus();
        return pingData.projectId() == status.projectId() && pingData.name().equals(status.name()) && pingData.version().equals(status.version());
    }
}