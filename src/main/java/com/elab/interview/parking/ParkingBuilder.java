package com.elab.interview.parking;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder class to get a parking instance
 */
public class ParkingBuilder {

    private int squareSize;
    private int maxSize;
    private Map<Integer, Character> parkingMap;

    public ParkingBuilder withSquareSize(final int size) {
        squareSize = size;
        maxSize = size * size;
        HashMap<Integer, Character> pm = new HashMap<>();
        for (int i = 0; i < maxSize; i++) {
            pm.put(i, 'U');
        }
        parkingMap = pm;
        return this;
    }

    public ParkingBuilder withPedestrianExit(final int pedestrianExitIndex) {
        if (pedestrianExitIndex < maxSize) {
            parkingMap.put(pedestrianExitIndex, '=');
        }
        return this;
    }

    public ParkingBuilder withDisabledBay(final int disabledBayIndex) {
        if (disabledBayIndex < maxSize) {
            parkingMap.put(disabledBayIndex, '@');
        }
        return this;
    }

    public Parking build() { // rirona una istanza di Parking
        return new Parking(parkingMap, squareSize);
    }
}
