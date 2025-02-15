package dev.wuffs.bcc.data;

public enum BetterStatusServerHolder {
    INSTANCE;

    BetterStatus status = null;

    public BetterStatus getStatus() {
        return status;
    }

    public void setStatus(BetterStatus status) {
        this.status = status;
    }
}
