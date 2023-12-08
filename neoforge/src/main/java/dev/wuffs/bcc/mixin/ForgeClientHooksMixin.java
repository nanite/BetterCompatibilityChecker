package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ServerStatus;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientHooks.class, remap = false)
public class ForgeClientHooksMixin {

    @Inject(method = "Lnet/neoforged/neoforge/client/ClientHooks;drawForgePingInfo(Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lnet/minecraft/client/gui/GuiGraphics;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(JoinMultiplayerScreen gui, ServerData oldTarget, GuiGraphics guiGraphics, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        ServerDataExtension data = ((ServerDataExtension) oldTarget);

        BetterStatus betterData = data.getBetterData();

        if (betterData == null || BetterStatusServerHolder.INSTANCE.getStatus() == null) {
            return;
        }

        BCCMultiplayerAddon.drawBCCChecker(gui, betterData, guiGraphics, x, y, width, relativeMouseX, relativeMouseY);
        ci.cancel();
    }

    @Inject(method = "processForgeListPingData(Lnet/minecraft/network/protocol/status/ServerStatus;Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processForgeListPingData(ServerStatus oldPacket, ServerData serverData, CallbackInfo ci) {
        ServerDataExtension data = (ServerDataExtension) serverData;
        BetterStatus betterData = data.getBetterData();
        if (betterData != null) {
            data.setBetterData(betterData);
            ci.cancel();
        }
    }
}
