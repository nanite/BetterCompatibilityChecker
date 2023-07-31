package dev.wuffs.bcc;

import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import dev.wuffs.bcc.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;


public class CommonClass {
    public static boolean comparePingData(BetterStatus pingData) {
        BetterStatus status = BetterStatusServerHolder.INSTANCE.getStatus();
        return pingData.projectId() == status.projectId() && pingData.name().equals(status.name()) && pingData.version().equals(status.version());
    }
}