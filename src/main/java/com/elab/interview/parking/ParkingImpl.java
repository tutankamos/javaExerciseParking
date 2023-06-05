package com.elab.interview.parking;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles the parking mechanisms: park/unpark a car (also for disabled-only bays) and provides a string representation of its state.
 */
class ParkingImpl implements Parking{

    private final List<Place> parkingPlaces;

    ParkingImpl(List<Place> parkingPlaces) { // todo come gestire questa visibilita' (va bene cosi ed e' normale che venga vista dal test?, ho provato facendo una interfaccia
        this.parkingPlaces = parkingPlaces;
    }

    /**
     * @return the number of available parking bays left
     */
    public int getAvailableBays() {
        return (int) parkingPlaces
                .stream()
                .filter(place -> {
                    Character info = place.getInfo();
                    return info == 'U' || info == '@';
                })
                .count();
    }

    /**
     * Park the car of the given type ('D' being dedicated to disabled people) in closest -to pedestrian exit- and first (starting from the parking's entrance)
     * available bay. Disabled people can only park on dedicated bays.
     *
     * @param carType the car char representation that has to be parked
     * @return bay index of the parked car, -1 if no applicable bay found
     */
    public int parkCar(final char carType) {
        int result;
        List<Place> places;

        if (carType == 'D') {
            places = parkingPlaces
                    .stream()
                    .filter(Place::isFreeDisabledBay)
                    .collect(Collectors.toList());
        } else {
            places = parkingPlaces
                    .stream()
                    .filter(Place::isFreeNormalBay)
                    .collect(Collectors.toList());
        }

        if (places.isEmpty()) {
            result = -1;
        } else {
            Place bestPlace = getBestPlace(places);
            bestPlace.setInfo(carType);

            result = bestPlace.getPoszionInParking();
        }

        return result;
    }

    /**
     * Unpark the car from the given index
     *
     * @param index
     * @return true if a car was parked in the bay, false otherwise
     */
    public boolean unparkCar(final int index) {
        if (index > parkingPlaces.size()) {
            return false;
        }

        Place place = parkingPlaces.get(index);
        if (place.getInfo() == '=' || place.getInfo() == '@' || place.getInfo() == 'U') {
            return false;
        } else if (place.getInfo() == 'D') {
            place.setInfo('@');
        } else {
            place.setInfo('U');
        }
        return true;
    }

    /**
     * Print a 2-dimensional representation of the parking with the following rules:
     * <ul>
     * <li>'=' is a pedestrian exit
     * <li>'@' is a disabled-only empty bay
     * <li>'U' is a non-disabled empty bay
     * <li>'D' is a disabled-only occupied bay
     * <li>the char representation of a parked vehicle for non-empty bays.
     * </ul>
     * U, D, @ and = can be considered as reserved chars.
     * <p>
     * Once an end of lane is reached, then the next lane is reversed (to represent the fact that cars need to turn around)
     *
     * @return the string representation of the parking as a 2-dimensional square. Note that cars do a U turn to continue to the next lane.
     */
    @Override
    public String toString() {
        String parkingInfoString = parkingPlaces.stream()
                .map(Place::getInfo)
                .map(Object::toString)
                .collect(Collectors.joining());

        StringBuilder parkingInfoStringbuilder = new StringBuilder(parkingInfoString);

        StringBuilder result = new StringBuilder();
        int squareSize = (int) Math.sqrt(parkingPlaces.size());
        for (int i = 0; i < squareSize; i++) {
            if (i % 2 == 0) {
                result.append(parkingInfoStringbuilder.substring(i * squareSize, i * squareSize + squareSize));
            } else {
                String s = new StringBuilder(parkingInfoStringbuilder.substring(i * squareSize, i * squareSize + squareSize)).reverse().toString();
                result.append(s);
            }
            if (!(i + 1 == squareSize)) {
                result.append('\n');
            }
        }
        return result.toString();
    }

    private Place getBestPlace(List<Place> places) {
        int minDistancePedestrianExit = Collections.min(places
                .stream()
                .map(Place::getDistancePedestrianExit)
                .collect(Collectors.toList()));
        Place bestPlace = places.stream()
                .filter(p -> {
                    return p.getDistancePedestrianExit() == minDistancePedestrianExit;
                })
                .collect(Collectors.toList())
                .get(0);
        return bestPlace;
    }
}