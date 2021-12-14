package dev.wuffs.bcc.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.PingData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BCCMultiplayerAddon {

    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");

    public static void drawBCCChecker(MultiplayerScreen gui, PingData pingData, MatrixStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        int idx;
        String tooltip;

        if (BCC.comparePingData(pingData)) {
            idx = 0;
            tooltip = ForgeI18n.parseMessage("bcc.gui.tooltip.compatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version));
            tooltip = tooltip + "\n \n" + TextFormatting.DARK_GRAY + "Better Compatibility Checker";
        } else {
            idx = 16;
            tooltip = ForgeI18n.parseMessage("bcc.gui.tooltip.incompatible_server", (pingData.name + " " + pingData.version), (BCC.localPingData.name + " " + BCC.localPingData.version));
            tooltip = tooltip + "\n \n" + TextFormatting.DARK_GRAY + "Better Compatibility Checker";
        }
        Minecraft.getInstance().getTextureManager().bind(ICON_SHEET);
        AbstractGui.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(StringTextComponent::new).collect(Collectors.toList()));
        }
    }
}
