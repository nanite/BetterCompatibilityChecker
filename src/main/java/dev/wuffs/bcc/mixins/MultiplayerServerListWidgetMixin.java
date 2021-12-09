package dev.wuffs.bcc.mixins;

import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
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
        @Shadow
        @Final
        private ServerInfo server;

        @Shadow
        @Final
        private MultiplayerScreen screen;

        @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIIIIIIZF)V", at = @At(value = "HEAD"))
        private void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
            int m = mouseX - x;
            int n = mouseY - y;
            BCCMultiplayerAddon.drawBCCPing(this.screen, this.server, matrices, x, y, entryWidth, m, n);
        }
    }

}
