package dev.wuffs.bcc;

public interface IServerStatus {
    void setModpackData(PingData modpackData);
    PingData getModpackData();
}
