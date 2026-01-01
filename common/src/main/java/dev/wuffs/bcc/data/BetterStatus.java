package dev.wuffs.bcc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BetterStatus(
        String name,
        String version,
        boolean isMetaData
) {
    public static Codec<BetterStatus> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                            Codec.STRING.optionalFieldOf("name", "??").forGetter(BetterStatus::name),
                            Codec.STRING.optionalFieldOf("version", "??").forGetter(BetterStatus::version),
                            Codec.BOOL.optionalFieldOf("isMetaData", false).forGetter(BetterStatus::isMetaData)
                    )
                    .apply(builder, BetterStatus::new)
    );
}
