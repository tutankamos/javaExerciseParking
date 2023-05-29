package com.elab.interview.parking;

import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles the parking mechanisms: park/unpark a car (also for disabled-only bays) and provides a string representation of its state.
 */
class Parking {

    private final List<Place> parkingPlaces;

    // todo cercare di impedire l'istanziazione di questa classe se non tramite il builder, forse basta mettere la classe come non pubblica, cosi la possono vedere solo nel pacchetto
    public Parking(List<Place> parkingPlaces) {
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
                    return info == 'U' | info == '@';
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
        Place place = parkingPlaces.get(index); //todo check for outofbound exception
        if (place.getInfo() == '=' | place.getInfo() == '@' | place.getInfo() == 'U') {
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
        StringBuilder stringOfResult = new StringBuilder();

        parkingPlaces.forEach(place -> {
            stringOfResult.append(place.getInfo());
        });

        StringBuilder result = new StringBuilder();
        int squareSize = (int) Math.sqrt(parkingPlaces.size());
        for (int i = 0; i < squareSize; i++) {
            if (i % 2 == 0) {
                result.append(stringOfResult.substring(i * squareSize, i * squareSize + squareSize -1));
                result.append('\n');
            } else {
                String s = new StringBuilder(stringOfResult.substring(i * 4, i * 4 + 3)).reverse().toString();
                result.append(s);
                result.append('\n');
            }
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}