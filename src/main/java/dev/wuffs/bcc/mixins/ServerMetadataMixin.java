package dev.wuffs.bcc.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.IServerMetadata;
import dev.wuffs.bcc.PingData;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerMetadata.class)
public class ServerMetadataMixin implements IServerMetadata {
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

    @Mixin(ServerMetadata.Deserializer.class)
    public static class ServerStatusSerializerMixin {

        @Inject(method = "serialize(Lnet/minecraft/server/ServerMetadata;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("RETURN"))
        private void serialize(ServerMetadata serverStatus, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir) {
            JsonObject jsonObject = cir.getReturnValue().getAsJsonObject();
            if (BCC.localPingData != null) {
                jsonObject.add("modpackData", jsonSerializationContext.serialize(BCC.localPingData, PingData.class));
            }
        }

        @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/server/ServerMetadata;", at = @At("RETURN"))
        private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ServerMetadata> cir) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("modpackData")) {
                IServerMetadata returnValue = (IServerMetadata) cir.getReturnValue();
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

