package dev.wuffs.bcc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

public record BetterStatus(
        int projectId,
        String name,
        String version,
        int versionId,
        String releaseType,
        boolean isMetaData
) {
    public static Codec<BetterStatus> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                Codec.INT.optionalFieldOf("projectId", -1).forGetter(BetterStatus::projectId),
                Codec.STRING.optionalFieldOf("name", "??").forGetter(BetterStatus::name),
                Codec.STRING.optionalFieldOf("version", "??").forGetter(BetterStatus::version),
                Codec.INT.optionalFieldOf("versionId", -1).forGetter(BetterStatus::versionId),
                Codec.STRING.optionalFieldOf("releaseType", "??").forGetter(BetterStatus::releaseType),
                Codec.BOOL.optionalFieldOf("isMetaData", false).forGetter(BetterStatus::isMetaData)
            )
            .apply(builder, BetterStatus::new)
    );
}
