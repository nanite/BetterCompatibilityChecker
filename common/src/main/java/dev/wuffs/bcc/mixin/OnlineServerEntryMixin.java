package dev.wuffs.bcc.mixin;

import dev.wuffs.bcc.BetterCompatibilityChecker;
import dev.wuffs.bcc.client.screen.CompatabilityRender;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.ServerDataExtension;
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
public abstract class OnlineServerEntryMixin extends ServerSelectionList.Entry {
    @Shadow
    @Final
    private ServerData serverData;

    @Shadow
    @Final
    private JoinMultiplayerScreen screen;

    @Inject(method = "renderContent", at = @At("TAIL"))
    private void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean whoKnows, float deltaTicks, CallbackInfo ci) {
//        int m = mouseX - x;
//        int n = mouseY - y;
        ServerDataExtension data = ((ServerDataExtension) serverData);
        BetterStatus betterData = data.getBetterData();

        if (betterData == null || BetterCompatibilityChecker.getBetterStatus() == null) {
            return;
        }

        CompatabilityRender.render(this.screen, betterData, guiGraphics, this.getContentX(), this.getContentY(), this.getContentWidth(), mouseX, mouseY);
    }
}
