package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.data.ExtendedServerStatus;
import dev.wuffs.bcc.data.ServerDataExtension;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class ServerStatusPingerMixin {

    @Shadow(aliases = {"val$p_105460_"})
    @Final
    ServerData val$data;

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
        ((ExtendedServerStatus) (Object) packet.status()).getBetterData().ifPresent(
                data -> ((ServerDataExtension) this.val$data).setBetterData(data)
        );
    }
}
