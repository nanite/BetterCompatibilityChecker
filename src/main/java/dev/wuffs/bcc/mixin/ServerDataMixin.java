package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.data.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerData.class)
public class ServerDataMixin implements ServerDataExtension {
    public BetterStatus betterStatus;

    @Override
    public void setBetterData(BetterStatus status) {
        this.betterStatus = status;
    }

    @Override
    public BetterStatus getBetterData() {
        return this.betterStatus;
    }
}
