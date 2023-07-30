package dev.wuffs.bcc;

public class Metadata {
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
