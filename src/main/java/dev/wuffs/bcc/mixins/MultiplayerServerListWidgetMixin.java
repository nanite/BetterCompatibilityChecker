package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.IServerInfo;
import dev.wuffs.bcc.PingData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MultiplayerServerListWidget.class)
public class MultiplayerServerListWidgetMixin {
    @Mixin(value = MultiplayerServerListWidget.ServerEntry.class)
    public static class ServerEntryMixin {
        private static final Identifier ICON_SHEET = new Identifier(BCC.MODID, "textures/gui/icons.png");
        @Shadow
        @Final
        private MinecraftClient client;

        @Shadow
        @Final
        private ServerInfo server;

        @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIIIIIIZF)V", at = @At("HEAD"))
        private void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
            int idx;
            if (this.server.online) {
                IServerInfo serverInfo = (IServerInfo) server;
                PingData pingData = serverInfo.getPingData();

                if (pingData != null) {
                    idx = 0;
                    RenderSystem.setShaderTexture(0, ICON_SHEET);
                    DrawableHelper.drawTexture(matrices, x + entryWidth - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);
//                    this.client.textRenderer.drawWithShadow(matrices, this.client.textRenderer.trimToWidth(pingData.name, entryWidth - 10), x + entryWidth - 18, y + 10, 0x00FF00);
                }
            }
        }
    }

}
