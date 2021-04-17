package com.majesty373.springgarden;

import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;

public class SpringGardenSession {
    @Getter(AccessLevel.PACKAGE)
    private int fruitGathered;

    @Getter(AccessLevel.PACKAGE)
    private int drinksMade;

    @Getter(AccessLevel.PACKAGE)
    private int failedRuns;

    @Getter(AccessLevel.PACKAGE)
    private int totalRuns;

    @Getter(AccessLevel.PACKAGE)
    private Instant lastFruitGathered;

    @Getter(AccessLevel.PACKAGE)
    private Instant lastDrinkMade;

    @Getter(AccessLevel.PACKAGE)
    private Instant lastFailedRun;

    void increaseFruitGathered() {
        fruitGathered++;
        totalRuns++;
        lastFruitGathered = Instant.now();
    }

    void increaseDrinksMade() {
        drinksMade++;
        lastDrinkMade = Instant.now();
    }

    void increaseFailedRuns() {
        failedRuns++;
        totalRuns++;
        lastFailedRun = Instant.now();
    }
}
