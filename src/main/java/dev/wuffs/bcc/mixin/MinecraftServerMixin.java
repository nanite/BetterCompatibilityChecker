package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import dev.wuffs.bcc.data.ExtendedServerStatus;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "buildServerStatus", at = @At("RETURN"))
    private void bcc$injectBetterStatus(CallbackInfoReturnable<ServerStatus> cir) {
        ServerStatus status = cir.getReturnValue();
        BetterStatus betterStatus = BetterStatusServerHolder.INSTANCE.getStatus();
        if (betterStatus == null) {
            return;
        }

        ((ExtendedServerStatus) (Object) status).setBetterData(betterStatus);
    }
}
