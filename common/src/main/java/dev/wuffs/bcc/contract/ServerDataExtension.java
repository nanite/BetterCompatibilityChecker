package dev.wuffs.bcc.contract;

import dev.wuffs.bcc.data.BetterStatus;

public interface ServerDataExtension {
    void setBetterData(BetterStatus status);

    BetterStatus getBetterData();
}
