package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.wuffs.bcc.*;
import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.ClientHooks;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientHooks.class, remap = false)
public class ForgeClientHooksMixin {

    /**
     * @author BetterCompatabilityChecker
     */
    @Inject(method = "drawForgePingInfo(Lnet/minecraft/client/gui/screen/MultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(MultiplayerScreen gui, ServerData oldTarget, MatrixStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        IServerData data = (IServerData) oldTarget;
        PingData pingData = data.getPingData();

        if (pingData == null || BCC.localPingData == null) {
            return;
        }
        BCCMultiplayerAddon.drawBCCChecker(gui, pingData, mStack, x, y, width, relativeMouseX, relativeMouseY);
        ci.cancel();
        ci.cancel();
    }

    @Inject(method = "processForgeListPingData(Lnet/minecraft/network/ServerStatusResponse;Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processForgeListPingData(ServerStatusResponse oldPacket, ServerData serverData, CallbackInfo ci) {
        IServerStatus packet = (IServerStatus) oldPacket;
        if (packet.getModpackData() != null) {
            IServerData data = (IServerData) serverData;
            data.setPingData(packet.getModpackData());
            ci.cancel();
        }
    }
}
