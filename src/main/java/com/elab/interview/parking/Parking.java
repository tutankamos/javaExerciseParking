package com.elab.interview.parking;

public interface Parking {
    int getAvailableBays();

    int parkCar(final char carType);

    boolean unparkCar(final int index);

    String toString();

}
