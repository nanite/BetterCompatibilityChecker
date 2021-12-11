package dev.wuffs.bcc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.PingData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BCCMultiplayerAddon {

    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");
    public static void drawBCCChecker(JoinMultiplayerScreen gui, PingData pingData, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY){
        int idx;
        String tooltip;

        if (BCC.comparePingData(pingData)) {
            idx = 0;
            tooltip = ChatFormatting.DARK_AQUA + "Server is running " + pingData.name + " " + pingData.version + "\n" + ChatFormatting.DARK_GREEN + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";
        } else {
            idx = 16;
            tooltip = ChatFormatting.GOLD + "You are not running the same version of the modpack as the server :(\n" + ChatFormatting.RED + "Server is running " + pingData.name + " " + pingData.version + "\n" + ChatFormatting.RED + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n \nProvided by Better Compatibility Checker";
//            tooltip = ChatFormatting.GOLD + "You are not running the same version of the modpack as the server :(\n \n" +
//                    ChatFormatting.RED + "Server: " + pingData.name + " " + pingData.version + "\n" +
//                    ChatFormatting.RED + "Client (You): " + BCC.localPingData.name + " " + BCC.localPingData.version +
//                    "\n \nProvided by Better Compatibility Checker";
        }
        RenderSystem.setShaderTexture(0, ICON_SHEET);
        GuiComponent.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(TextComponent::new).collect(Collectors.toList()));
        }
    }
}
