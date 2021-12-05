package dev.wuffs.bcc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.IntValue modpackProjectID;
    public static ForgeConfigSpec.ConfigValue<String> modpackName;
    public static ForgeConfigSpec.ConfigValue<String> modpackVersion;
    public static ForgeConfigSpec.BooleanValue useMetadata;


    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        modpackProjectID = BUILDER.comment("The CurseForge project ID for the modpack")
                .defineInRange("modpackProjectID", 0, 0, Integer.MAX_VALUE);
        modpackName = BUILDER.comment("The name of the modpack")
                .define("modpackName", "PLEASE_CHANGE_ME");
        modpackVersion = BUILDER.comment("The version of the modpack")
                .define("modpackVersion", "CHANGE_ME");
        useMetadata = BUILDER.comment("Use the metadata.json to determine the modpack version", "ONLY ENABLE THIS IF YOU KNOW WHAT YOU ARE DOING")
                .define("useMetadata", false);

        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}
