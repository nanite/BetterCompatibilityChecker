package dev.wuffs.bcc;

public class PingData {
    public Manifest manifest;
    public int projectID;
    public String name;
    public String version;
    public static class Manifest {
        public String id;
        public String name;
        public String type;
        public Version version;

        public static class Version {
            public int id;
            public String name;
            public String type;
        }
    }
}
