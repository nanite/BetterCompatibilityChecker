package dev.wuffs.bcc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.IServerInfo;
import dev.wuffs.bcc.PingData;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BCCMultiplayerAddon {
    private static final Identifier ICON_SHEET = new Identifier(BCC.MODID, "textures/gui/icons.png");

    public static void drawBCCPing(MultiplayerScreen gui, ServerInfo oldTarget, MatrixStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        IServerInfo data = (IServerInfo) oldTarget;
        PingData pingData = data.getPingData();
        int idx;
        String tooltip;

        if (pingData == null) {
            return;
        }

        if (BCC.comparePingData(pingData)) {
            idx = 0;
            tooltip = Text.translatable("bcc.gui.tooltip.compatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version)).getString();
            tooltip = tooltip + "\n \n" + Formatting.DARK_GRAY + "Better Compatibility Checker";
        } else {
            idx = 16;
            tooltip = Text.translatable("bcc.gui.tooltip.incompatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version)).getString();
            tooltip = tooltip + "\n \n" + Formatting.DARK_GRAY + "Better Compatibility Checker";
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, ICON_SHEET);
        DrawableHelper.drawTexture(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 16, 32);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setTooltip(Arrays.stream(tooltip.split("\n")).map(Text::literal).collect(Collectors.toList()));
        }
    }
}