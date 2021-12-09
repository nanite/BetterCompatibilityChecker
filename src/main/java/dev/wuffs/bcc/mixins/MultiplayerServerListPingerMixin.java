package dev.wuffs.bcc.mixins;

import dev.wuffs.bcc.IServerInfo;
import dev.wuffs.bcc.IServerMetadata;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(targets = "net.minecraft.client.network.MultiplayerServerListPinger$1")
public class MultiplayerServerListPingerMixin {

    @Inject(method = "onResponse", at = @At("HEAD"))
    public void onResponse(QueryResponseS2CPacket packet, CallbackInfo ci) {
        IServerMetadata serverMetadata = (IServerMetadata) packet.getServerMetadata();
        if (serverMetadata.getModpackData() != null) {
            IServerInfo serverInfo = (IServerInfo) serverMetadata.getModpackData();
            serverInfo.setPingData(serverMetadata.getModpackData());
        }
    }
}
