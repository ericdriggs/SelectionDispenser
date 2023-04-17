package io.github.ericdriggs.selectiondispenser.dispenser.turnstyle;

import java.util.Date;

/**
 * Example T for testing
 */
public class TurnstileEvent {

    private final Date firedOn = new Date();
    private final TurnstileLane lane;

    TurnstileEvent(TurnstileLane lane) {
        this.lane = lane;
    }

    public Date getFiredOn() {
        return firedOn;
    }

    public TurnstileLane getLane() {
        return lane;
    }
}