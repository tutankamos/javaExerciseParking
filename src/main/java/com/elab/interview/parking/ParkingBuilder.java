package com.elab.interview.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder class to get a parking instance
 */
public class ParkingBuilder {

    private final Parking parking;
    private final List<Place> parkingPlaces = new ArrayList<>();
    private final List<Integer> exitPositionIndexes = new ArrayList<>();
    private int maxSize;

    public ParkingBuilder() {
        parking = new ParkingImpl(parkingPlaces);
    }

    public ParkingBuilder withSquareSize(final int size) {
        maxSize = size * size;
        for (int i = 0; i < maxSize; i++) {
            parkingPlaces.add(new Place(i, 'U'));
        }
        return this;
    }

    public ParkingBuilder withPedestrianExit(final int pedestrianExitIndex) {
        if (pedestrianExitIndex < maxSize && parkingPlaces.size() > 0) {
            parkingPlaces.get(pedestrianExitIndex).setInfo('=');
            exitPositionIndexes.add(pedestrianExitIndex);
        }
        return this;
    }

    public ParkingBuilder withDisabledBay(final int disabledBayIndex) {
        if (disabledBayIndex < maxSize && parkingPlaces.size() > 0) {
            parkingPlaces.get(disabledBayIndex).setInfo('@');
        }
        return this;
    }

    public Parking build() {
        // todo lancia un eccezione se parkingPlaces.size = 0
        if (!exitPositionIndexes.isEmpty()) {
            calculateDistancePedestrianExit(parkingPlaces);
        }
        return parking;
    }

    void calculateDistancePedestrianExit(List<Place> parkingPlaces) {
        // 0 1 2 3 4 5 6 7
        // 0 0 = 0 0 0 0 0
        // 1 2 = 1 2 3 4 5
        // 2 1 = 1 2 3 4 5
        int counter = 1;
        int i = exitPositionIndexes.get(0);
        while (i < parkingPlaces.size()) {
            if (parkingPlaces.get(i).isPedestrianExit()) {
                i++;
                counter = 1;
            } else {
                parkingPlaces.get(i).setDistancePedestrianExit(counter);
                counter++;
                i++;
            }
        }
        counter = 1;
        i = exitPositionIndexes.get(exitPositionIndexes.size() - 1);
        while (i >= 0) {
            if (parkingPlaces.get(i).isPedestrianExit()) {
                counter = 1;
                i--;
            } else {
                if (parkingPlaces.get(i).getDistancePedestrianExit() > counter || parkingPlaces.get(i).getDistancePedestrianExit() == 0) {
                    parkingPlaces.get(i).setDistancePedestrianExit(counter);
                    counter++;
                }
                i--;
            }
        }
    }
}