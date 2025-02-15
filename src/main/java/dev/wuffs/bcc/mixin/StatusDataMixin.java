package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerStatus.class)
public class StatusDataMixin implements ServerDataExtension {
    private BetterStatus betterStatus; // TODO: This might not be safe being null. I don't understand codecs

    public void setBetterData(BetterStatus status) {
        this.betterStatus = status;
    }

    public BetterStatus getBetterData() {
        return this.betterStatus;
    }
}