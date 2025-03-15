package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientboundStatusResponsePacket.class)
public class ClientboundStatusResponsePacketMixin {
    @Shadow
    @Final
    private ServerStatus status;

    @Inject(method = "write", at = @At("TAIL"))
    void onWrite(FriendlyByteBuf p_134899_, CallbackInfo ci) {
        p_134899_.writeJsonWithCodec(BetterStatus.CODEC, BetterStatusServerHolder.INSTANCE.getStatus());
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("TAIL"))
    void onReadConstructor(FriendlyByteBuf buffer, CallbackInfo ci) {
        // Ensure the byte reader doesn't go out of range as non-modded servers / servers without our mod will not be able
        // to read the json bytes that do not exist
        if (buffer.readerIndex() + 1 > buffer.capacity()) {
            return;
        }

        ((ServerDataExtension) (Object) status).setBetterData(buffer.readJsonWithCodec(BetterStatus.CODEC));
    }
}
