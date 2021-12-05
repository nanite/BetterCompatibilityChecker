package dev.wuffs.bcc.mixins;

import dev.wuffs.bcc.IServerData;
import dev.wuffs.bcc.PingData;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerData.class)
public class ServerDataMixin implements IServerData {
    private PingData pingData = null;

    @Override
    public PingData getPingData() {
        return this.pingData;
    }

    @Override
    public void setPingData(PingData pingData) {
        this.pingData = pingData;
    }
}
