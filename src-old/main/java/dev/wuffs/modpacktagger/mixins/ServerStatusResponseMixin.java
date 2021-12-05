package dev.wuffs.modpacktagger.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.wuffs.modpacktagger.IServerStatusReponse;
import dev.wuffs.modpacktagger.ModpackTagger;
import net.minecraft.network.ServerStatusResponse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerStatusResponse.class)
public class ServerStatusResponseMixin implements IServerStatusReponse {

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

    @Mixin(ServerStatusResponse.Serializer.class)
    public static class ServerStatusResponseSerializerMixin {
        @ModifyVariable(method = "serialize(Lnet/minecraft/network/ServerStatusResponse;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("STORE"), remap = false)
        private JsonObject injected(JsonObject jsonObject) {
            jsonObject.add("modpack", ModpackTagger.metaData);
            return jsonObject;
        }

        @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/ServerStatusResponse;", at = @At("RETURN"), remap = false)
        private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ServerStatusResponse> cir) {
            ServerStatusResponse serverStatusResponse = cir.getReturnValue();
//            ((IServerStatusReponse) serverStatusResponse).setModpackData(jsonElement.getAsJsonObject().get("modpack").getAsString());
        }
    }
}

