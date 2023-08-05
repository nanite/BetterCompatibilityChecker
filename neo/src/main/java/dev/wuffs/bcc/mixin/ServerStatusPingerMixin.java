package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class ServerStatusPingerMixin {
    private static final Field SERVER_DATA_FIELD;

    // Credit to https://github.com/Aizistral-Studios/No-Chat-Reports/blob/1.20-Unified/forge/src/main/java/com/aizistral/nochatreports/forge/mixins/client/MixinServerStatusPinger%241.java
    static {
        // We use reflection here because Mixin's AP dies when trying to process @Shadow of this lol
        try {
            Class<?> pinger = Class.forName("net.minecraft.client.multiplayer.ServerStatusPinger$1");

            SERVER_DATA_FIELD = pinger.getDeclaredField("val$p_105460_");
            SERVER_DATA_FIELD.setAccessible(true);
        } catch (Exception ex) {
            Error error = new Error("Reflection failed in MixinServerStatusPinger$1!", ex);

            Minecraft.getInstance().execute(() -> {
                throw error;
            });

            throw error;
        }
    }

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
        try {
            ((ServerDataExtension) SERVER_DATA_FIELD.get(this)).setBetterData(betterData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}