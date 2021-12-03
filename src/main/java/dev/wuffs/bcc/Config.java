package dev.wuffs.bcc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue debug;
    public static ForgeConfigSpec.IntValue modpackProjectID;
    public static ForgeConfigSpec.ConfigValue<String> modpackName;
    public static ForgeConfigSpec.ConfigValue<String> modpackVersion;
    public static ForgeConfigSpec.BooleanValue useManifest;


    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        modpackProjectID = BUILDER.comment("The CurseForge project ID for the modpack")
                .defineInRange("modpackProjectID", 0, 0, Integer.MAX_VALUE);
        modpackName = BUILDER.comment("The name of the modpack")
                .define("modpackName", "PLEASE_CHANGE_ME");
        modpackVersion = BUILDER.comment("The version of the modpack")
                .define("modpackVersion", "CHANGE_ME");
        useManifest = BUILDER.comment("Use the manifest.json to determine the modpack version", "ONLY ENABLE THIS IF YOU KNOW WHAT YOU ARE DOING")
                .define("useManifest", false);
        debug = BUILDER.comment("Enable debug mode")
                .define("debug", false);

        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}
