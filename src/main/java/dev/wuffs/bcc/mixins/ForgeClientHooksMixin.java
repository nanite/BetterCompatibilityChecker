package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.*;
import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
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

    @Inject(method = "drawForgePingInfo(Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(JoinMultiplayerScreen gui, ServerData oldTarget, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        ServerDataExtension data = ((ServerDataExtension) oldTarget);

        BetterStatus betterData = data.getBetterData();

        if (betterData == null || BetterStatusServerHolder.INSTANCE.getStatus() == null) {
            return;
        }

        BCCMultiplayerAddon.drawBCCChecker(gui, betterData, mStack, x, y, width, relativeMouseX, relativeMouseY);
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
