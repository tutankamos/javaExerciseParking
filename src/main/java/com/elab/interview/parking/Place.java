package com.elab.interview.parking;

public class Place {
    private final int poszionInParking;
    private Character info;
    private int distancePedestrianExit;

    public Place(int poszionInParking, Character info) {
        this.poszionInParking = poszionInParking;
        this.info = info;
    }

    // METHODS
    boolean isPedestrianExit() {
        return info.equals('=');
    }

    // GET AND SETTERS


    public int getDistancePedestrianExit() {
        return distancePedestrianExit;
    }

    public void setDistancePedestrianExit(int distancePedestrianExit) {
        this.distancePedestrianExit = distancePedestrianExit;
    }

    public int getPoszionInParking() {
        return poszionInParking;
    }

    public Character getInfo() {
        return info;
    }

    public void setInfo(Character info) {
        this.info = info;
    }
}
