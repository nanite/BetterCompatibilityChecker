package dev.wuffs.bcc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.PingData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BCCMultiplayerAddon {

    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");

    public static void drawBCCChecker(JoinMultiplayerScreen gui, PingData pingData, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        int idx;
        String tooltip;

        if (BCC.comparePingData(pingData)) {
            idx = 0;
            tooltip = ForgeI18n.parseMessage("bcc.gui.tooltip.compatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version));
        } else {
            idx = 16;
            tooltip = ForgeI18n.parseMessage("bcc.gui.tooltip.incompatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version));
        }
        tooltip = tooltip + "\n \n" + ChatFormatting.DARK_GRAY + "Better Compatibility Checker";
        RenderSystem.setShaderTexture(0, ICON_SHEET);
        GuiComponent.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(Component::literal).collect(Collectors.toList()));
        }
    }
}
