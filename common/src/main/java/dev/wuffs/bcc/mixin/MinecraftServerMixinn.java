package dev.wuffs.bcc.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.BetterStatusServerHolder;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixinn {
    @Inject(method = "getStatusJson", at = @At("RETURN"), cancellable = true, remap = false)
    public void modifyStatusJson(CallbackInfoReturnable<String> cir) {
        JsonElement jsonElement = JsonParser.parseString(cir.getReturnValue());

        JsonElement betterStatus = BetterStatus.CODEC.encodeStart(JsonOps.INSTANCE, BetterStatusServerHolder.INSTANCE.getStatus())
                .result()
                .orElse(null);

        // TODO: Error catching. If error, return original.
        jsonElement.getAsJsonObject().add("better-status", betterStatus);
        cir.setReturnValue(new Gson().toJson(jsonElement));
    }
}
