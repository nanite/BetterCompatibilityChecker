package dev.wuffs.bcc.client.screen;

import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.I18nExtension;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

import java.util.Arrays;
import java.util.stream.Collectors;

import static dev.wuffs.bcc.CommonClass.comparePingData;

public class BCCMultiplayerAddon {

    private static final ResourceLocation ICON_SHEET = new ResourceLocation(NeoForgeVersion.MOD_ID, "textures/gui/icons.png");

    public static void drawBCCChecker(JoinMultiplayerScreen gui, BetterStatus pingData, GuiGraphics guiGraphics, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        int idx;
        String tooltip;

        if (comparePingData(pingData)) {
            idx = 0;
            tooltip = I18nExtension.parseMessage("bcc.gui.tooltip.compatible_server", (pingData.name() + " " + pingData.version()), (BetterStatusServerHolder.INSTANCE.getStatus().name() + " " + BetterStatusServerHolder.INSTANCE.getStatus().version()));
        } else {
            idx = 16;
            tooltip = I18nExtension.parseMessage("bcc.gui.tooltip.incompatible_server", (pingData.name() + " " + pingData.version()), (BetterStatusServerHolder.INSTANCE.getStatus().name() + " " + BetterStatusServerHolder.INSTANCE.getStatus().version()));
        }

        tooltip = tooltip + "\n \n" + ChatFormatting.DARK_GRAY + "Better Compatibility Checker";
        guiGraphics.blit(ICON_SHEET, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(Component::literal).collect(Collectors.toList()));
        }
    }
}
