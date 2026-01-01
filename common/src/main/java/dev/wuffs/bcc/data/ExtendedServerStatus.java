package dev.wuffs.bcc.data;

import java.util.Optional;

public interface ExtendedServerStatus {
    Optional<BetterStatus> getBetterData();

    void setBetterData(BetterStatus status);
}
