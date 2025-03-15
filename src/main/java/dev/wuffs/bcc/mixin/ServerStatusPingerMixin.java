package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
//@Mixin(ServerStatusPinger.class)
public class ServerStatusPingerMixin {

    @Shadow(aliases = {"val$data"}) @Final
    ServerData val$serverData;

    @Inject(
            method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/status/ServerStatus;description()Lnet/minecraft/network/chat/Component;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    void onHandleResponse(ClientboundStatusResponsePacket packet, CallbackInfo ci) {
        BetterStatus betterData = ((ServerDataExtension) (Object) packet.status()).getBetterData();
        ((ServerDataExtension) val$serverData).setBetterData(betterData);
    }
}
