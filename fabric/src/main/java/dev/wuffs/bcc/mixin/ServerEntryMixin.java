package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.client.screen.BCCMultiplayerAddon;
import dev.wuffs.bcc.contract.ServerDataExtension;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = ServerSelectionList.OnlineServerEntry.class)
public class ServerEntryMixin {
    @Shadow
    @Final
    private ServerData serverData;

    @Shadow
    @Final
    private JoinMultiplayerScreen screen;

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At(value = "HEAD"))
    private void render(GuiGraphics guiGraphics, int i, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        int m = mouseX - x;
        int n = mouseY - y;
        ServerDataExtension data = ((ServerDataExtension) serverData);
        BetterStatus betterData = data.getBetterData();

        if (betterData == null || BetterStatusServerHolder.INSTANCE.getStatus() == null) {
            return;
        }
        BCCMultiplayerAddon.drawBCCChecker(this.screen, betterData, guiGraphics, x, y, entryWidth, m, n);
    }
}
