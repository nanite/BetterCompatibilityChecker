package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class ForgeClientHooksMixin {
    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");

    /**
     * @author BetterCompatabilityChecker
     */
    @Inject(method = "drawForgePingInfo(Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(JoinMultiplayerScreen gui, ServerData oldTarget, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        IServerData data = (IServerData) oldTarget;
        PingData pingData = data.getPingData();
        int idx;
        String tooltip;
        if (pingData == null) {
            return;
        }

        if (BCC.comparePingData(pingData)) {
            idx = 0;
            tooltip = ChatFormatting.DARK_AQUA + "Server is running " + pingData.name + " " + pingData.version + "\n" + ChatFormatting.DARK_GREEN + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";
        } else {
            idx = 16;
            tooltip = ChatFormatting.GOLD + "You are not running the same version of the modpack as the server :(\n" + ChatFormatting.RED + "Server is running " + pingData.name + " " + pingData.version + "\n" + ChatFormatting.RED + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";

        }
        RenderSystem.setShaderTexture(0, ICON_SHEET);
        GuiComponent.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(TextComponent::new).collect(Collectors.toList()));
        }
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
