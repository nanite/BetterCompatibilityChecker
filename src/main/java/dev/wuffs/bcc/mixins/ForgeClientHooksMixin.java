package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.*;
import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class ForgeClientHooksMixin {
    /**
     * @author BetterCompatabilityChecker
     */
    @Inject(method = "drawForgePingInfo(Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(JoinMultiplayerScreen gui, ServerData oldTarget, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        IServerData data = (IServerData) oldTarget;
        PingData pingData = data.getPingData();

        if (pingData == null || BCC.localPingData == null) {
            return;
        }
        BCCMultiplayerAddon.drawBCCChecker(gui, pingData, mStack, x, y, width, relativeMouseX, relativeMouseY);
        ci.cancel();
    }

    @Inject(method = "processForgeListPingData(Lnet/minecraft/network/protocol/status/ServerStatus;Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processForgeListPingData(ServerStatus oldPacket, ServerData serverData, CallbackInfo ci) {
        IServerStatus packet = (IServerStatus) oldPacket;
        if (packet.getModpackData() != null) {
            IServerData data = (IServerData) serverData;
            data.setPingData(packet.getModpackData());
            ci.cancel();
        }
    }
}
