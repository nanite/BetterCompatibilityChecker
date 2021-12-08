package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.wuffs.bcc.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.ClientHooks;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(value = ClientHooks.class, remap = false)
public class ForgeClientHooksMixin {
    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");

    /**
     * @author BetterCompatabilityChecker
     */
    @Inject(method = "drawForgePingInfo(Lnet/minecraft/client/gui/screen/MultiplayerScreen;Lnet/minecraft/client/multiplayer/ServerData;Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private static void drawForgePingInfo(MultiplayerScreen gui, ServerData oldTarget, MatrixStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY, CallbackInfo ci) {
        IServerData data = (IServerData) oldTarget;
        PingData pingData = data.getPingData();
        int idx;
        String tooltip;
        if (pingData == null) {
            return;
        }

        if ((Objects.equals(pingData.name, BCC.localPingData.name)) && (Objects.equals(pingData.version, BCC.localPingData.version))) {
            idx = 0;
            tooltip = TextFormatting.DARK_AQUA + "Server is running " + pingData.name + " " + pingData.version + "\n" + TextFormatting.DARK_GREEN + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";
        } else {
            idx = 16;
            tooltip = TextFormatting.GOLD + "You are not running the same version of the modpack as the server :(\n" + TextFormatting.RED + "Server is running " + pingData.name + " " + pingData.version + "\n" + TextFormatting.RED + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";

        }
        Minecraft.getInstance().getTextureManager().bind(ICON_SHEET);
        AbstractGui.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            //this is not the most proper way to do it,
            //but works best here and has the least maintenance overhead
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(StringTextComponent::new).collect(Collectors.toList()));
        }
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
