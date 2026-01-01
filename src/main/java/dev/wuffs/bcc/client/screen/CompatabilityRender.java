package dev.wuffs.bcc.client.screen;

import dev.wuffs.bcc.BetterCompatibilityChecker;
import dev.wuffs.bcc.data.BetterStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static dev.wuffs.bcc.BetterCompatibilityChecker.comparePingData;

public class CompatabilityRender {
    private static final ResourceLocation ICON_SHEET = ResourceLocation.fromNamespaceAndPath(BetterCompatibilityChecker.MOD_ID, "textures/gui/icons.png");

    public static void render(JoinMultiplayerScreen gui, BetterStatus serverStatus, GuiGraphics guiGraphics, int x, int y, int width, int mouseX, int mouseY) {
        if (serverStatus == null) {
            return;
        }

        var localStatus = BetterCompatibilityChecker.getBetterStatus();
        if (localStatus == null) {
            return;
        }

        int idx = 0;
        String langKey = "bcc.gui.tooltip.compatible_server";
        if (!comparePingData(serverStatus)) {
            idx = 16;
            langKey = "bcc.gui.tooltip.incompatible_server";
        }

        String tooltip = Component.translatable(langKey, (serverStatus.name() + " " + serverStatus.version()), (localStatus.name() + " " + localStatus.version())).getString();
        tooltip = tooltip + "\n \n" + ChatFormatting.DARK_GRAY + "Better Compatibility Checker";

        guiGraphics.blit(ICON_SHEET, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 16, 32);

        if (mouseX > width - 15 && mouseX < width && mouseY > 10 && mouseY < 26) {
            gui.setTooltipForNextRenderPass(Minecraft.getInstance().font.split(Component.literal(tooltip), 370));
        }
    }
}
