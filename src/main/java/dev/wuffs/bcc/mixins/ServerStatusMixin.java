package dev.wuffs.bcc.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.Config;
import dev.wuffs.bcc.IServerStatus;
import dev.wuffs.bcc.PingData;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerStatus.class)
public class ServerStatusMixin implements IServerStatus {
    private String modpackData;

    @Override
    public void setModpackData(String modpackData) {
        this.modpackData = modpackData;
    }

    @Override
    public String getModpackData() {
        return this.modpackData;
    }

    @Mixin(ServerStatus.Serializer.class)
    public static class ServerStatusSerializerMixin {

        @ModifyVariable(method = "serialize(Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("STORE"), remap = false)
        private JsonObject injected(JsonObject jsonObject) {
            if (Config.useManifest.get()) {
                jsonObject.add("modpack", BCC.metaData);
            } else {
                JsonObject modpack = new JsonObject();
                modpack.addProperty("id", Config.modpackProjectID.get());
                modpack.addProperty("name", Config.modpackName.get());
                modpack.addProperty("version", Config.modpackVersion.get());
                jsonObject.add("modpack", modpack);
            }
            return jsonObject;
        }

        @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/protocol/status/ServerStatus;", at = @At("RETURN"), remap = false)
        private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ServerStatus> cir) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("modpack")) {
                if (Config.useManifest.get()) {
                    BCC.remotePingData = context.deserialize(jsonObject.get("modpack"), PingData.Manifest.class);
                }else {
                    JsonElement modpackData = jsonObject.get("modpack");
                    BCC.remotePingData.projectID = modpackData.getAsJsonObject().get("id").getAsInt();
                    BCC.remotePingData.name = modpackData.getAsJsonObject().get("name").getAsString();
                    BCC.remotePingData.version = modpackData.getAsJsonObject().get("version").getAsString();
                }
            }
        }
    }
}

