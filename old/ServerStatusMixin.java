package dev.wuffs.bcc.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.Config;
import dev.wuffs.bcc.IServerStatus;
import dev.wuffs.bcc.PingData;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerStatus.class)
public class ServerStatusMixin implements IServerStatus {
    private PingData modpackData;

    @Override
    public PingData getModpackData() {
        return this.modpackData;
    }

    @Override
    public void setModpackData(PingData modpackData) {
        this.modpackData = modpackData;
//        this.invalidateJson();
    }

    @Mixin(ServerStatus.Serializer.class)
    public static class ServerStatusSerializerMixin {

        @Inject(method = "serialize(Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("RETURN"), remap = false)
        private void serialize(ServerStatus serverStatus, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir) {
            JsonObject jsonObject = cir.getReturnValue().getAsJsonObject();
            jsonObject.remove("forgeData");
            jsonObject.add("modpackData", jsonSerializationContext.serialize(BCC.localPingData, PingData.class));
        }

        @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/protocol/status/ServerStatus;", at = @At("RETURN"), remap = false)
        private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ServerStatus> cir) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("modpackData")) {
                IServerStatus returnValue = (IServerStatus) cir.getReturnValue();
                JsonElement modpackData = jsonObject.get("modpackData");
                JsonObject modpack = modpackData.getAsJsonObject();
                PingData pingData = new PingData();
                pingData.projectID = modpack.get("projectID").getAsInt();
                pingData.name = modpack.get("name").getAsString();
                pingData.version = modpack.get("version").getAsString();
                pingData.isMetadata = modpack.get("isMetadata").getAsBoolean();
                returnValue.setModpackData(pingData);
            }
        }
    }
}

