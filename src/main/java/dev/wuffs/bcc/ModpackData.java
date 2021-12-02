package dev.wuffs.bcc;

public class ModpackData {
    public String launcher;
    public int id;
    public String name;
    public String type;
    public Version version;

    public static class Version {
        public int id;
        public String name;
        public String type;
    }
}
