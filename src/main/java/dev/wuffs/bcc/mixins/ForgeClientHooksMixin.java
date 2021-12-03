package dev.wuffs.bcc.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class ForgeClientHooksMixin {
    private static final ResourceLocation ICON_SHEET = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/icons.png");

    /**
     * @author BetterCompatabilityChecker
     * @reason If the server is sending the modpack and version use it to check if compatible or not
     */
    @Overwrite
    public static void drawForgePingInfo(JoinMultiplayerScreen gui, ServerData target, PoseStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        int idx;
        String tooltip;
        if (BCC.remotePingData == null || BCC.localPingData == null || target.forgeData == null)
            return;
        switch (target.forgeData.type) {
            case "FML":
                if (Config.useManifest.get()) {
                    if ((BCC.remotePingData.manifest.id == BCC.localPingData.manifest.id) && (BCC.remotePingData.manifest.version.id == BCC.localPingData.manifest.version.id)) {
                        idx = 0;
                        tooltip = ChatFormatting.DARK_AQUA + "Server is running " + BCC.remotePingData.manifest.name + " " + BCC.remotePingData.manifest.version.id + "\n" + ChatFormatting.DARK_GREEN + "Your version is " + BCC.localPingData.manifest.name + " " + BCC.localPingData.manifest.version.id + "\n";
                    } else {
                        idx = 16;
                        tooltip = ChatFormatting.DARK_RED + "You are not running the same version of the modpack as the server :(\n" + ChatFormatting.RED + "Server is running " + BCC.remotePingData.manifest.name + " " + BCC.remotePingData.manifest.version.id + "\n" + ChatFormatting.RED + "Your version is " + BCC.localPingData.manifest.name + " " + BCC.localPingData.manifest.version.id;

                    }
                } else {
                    if ((BCC.remotePingData.name.equals(BCC.localPingData.name)) && (BCC.remotePingData.version.equals(BCC.localPingData.version))) {
                        idx = 0;
                        tooltip = ChatFormatting.DARK_AQUA + "Server is running " + BCC.remotePingData.name + " " + BCC.remotePingData.version + "\n" + ChatFormatting.DARK_GREEN + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version + "\n";
                    } else {
                        idx = 16;
                        tooltip = ChatFormatting.DARK_RED + "You are not running the same version of the modpack as the server :(\n" + ChatFormatting.RED + "Server is running " + BCC.remotePingData.name + " " + BCC.remotePingData.version + "\n" + ChatFormatting.RED + "Your version is " + BCC.localPingData.name + " " + BCC.localPingData.version;

                    }
                }

                break;
            case "VANILLA":
                if (target.forgeData.isCompatible) {
                    idx = 48;
                    tooltip = ForgeI18n.parseMessage("fml.menu.multiplayer.vanilla");
                } else {
                    idx = 80;
                    tooltip = ForgeI18n.parseMessage("fml.menu.multiplayer.vanilla.incompatible");
                }
                break;
            default:
                idx = 64;
                tooltip = ForgeI18n.parseMessage("fml.menu.multiplayer.unknown", target.forgeData.type);
        }

        RenderSystem.setShaderTexture(0, ICON_SHEET);
        GuiComponent.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if (relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            //this is not the most proper way to do it,
            //but works best here and has the least maintenance overhead
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(TextComponent::new).collect(Collectors.toList()));
        }
    }
}
