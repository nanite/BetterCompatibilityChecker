package dev.wuffs.bcc.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wuffs.bcc.data.BetterStatus;
import dev.wuffs.bcc.data.ExtendedServerStatus;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(ServerStatus.class)
public class ServerStatusMixin implements ExtendedServerStatus {
    @Shadow
    @Final
    @Mutable
    public static Codec<ServerStatus> CODEC;

    @Shadow
    @Final
    private Component description;
    @Unique
    private Optional<BetterStatus> bcc$betterStatus = Optional.empty();

    /**
     * I'd really love it if we didn't need to completely recreate the codec here but alas, it seems to be the only way.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bcc$onClassInit(CallbackInfo ci) {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ComponentSerialization.CODEC.lenientOptionalFieldOf("description", CommonComponents.EMPTY).forGetter(ServerStatus::description),
                ServerStatus.Players.CODEC.lenientOptionalFieldOf("players").forGetter(ServerStatus::players),
                ServerStatus.Version.CODEC.lenientOptionalFieldOf("version").forGetter(ServerStatus::version),
                ServerStatus.Favicon.CODEC.lenientOptionalFieldOf("favicon").forGetter(ServerStatus::favicon),
                Codec.BOOL.lenientOptionalFieldOf("enforcesSecureChat", false).forGetter(ServerStatus::enforcesSecureChat),
                BetterStatus.CODEC.lenientOptionalFieldOf("betterStatus").forGetter(status -> ((ExtendedServerStatus) (Object) status).getBetterData())
        ).apply(instance, (description, players, version, favicon, enforcesSecureChat, betterStatus) -> {
            ServerStatus status = new ServerStatus(description, players, version, favicon, enforcesSecureChat);
            ((ExtendedServerStatus) (Object) status).setBetterData(betterStatus.orElse(null));
            return status;
        }));
    }

    @Override
    public Optional<BetterStatus> getBetterData() {
        return this.bcc$betterStatus;
    }

    @Override
    public void setBetterData(BetterStatus status) {
        this.bcc$betterStatus = Optional.ofNullable(status);
    }
}
