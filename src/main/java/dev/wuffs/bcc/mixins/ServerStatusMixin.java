package dev.wuffs.bcc.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.wuffs.bcc.BCC;
import dev.wuffs.bcc.IServerStatus;
import dev.wuffs.bcc.ModpackData;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerStatus.class)
public class ServerStatusMixin implements IServerStatus {

//    @Shadow
//    public abstract void invalidateJson();

    private String modpackData;

    @Override
    public void setModpackData(String modpackData) {
        this.modpackData = modpackData;
//        this.invalidateJson();
    }

    @Override
    public String getModpackData() {
        return this.modpackData;
    }

    @Mixin(ServerStatus.Serializer.class)
    public static class ServerStatusSerializerMixin {
        @ModifyVariable(method = "serialize(Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("STORE"), remap = false)
        private JsonObject injected(JsonObject jsonObject) {
            jsonObject.add("modpack", BCC.metaData);
            return jsonObject;
        }

        @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/protocol/status/ServerStatus;", at = @At("RETURN"), remap = false)
        private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ServerStatus> cir) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("modpack")) {
                BCC.remoteModpackData = context.deserialize(jsonObject.get("modpack"), ModpackData.class);
            }
        }
    }
}

