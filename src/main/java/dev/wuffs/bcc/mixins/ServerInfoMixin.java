package dev.wuffs.bcc.mixins;

import dev.wuffs.bcc.IServerInfo;
import dev.wuffs.bcc.PingData;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerInfo.class)
public class ServerInfoMixin implements IServerInfo {
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