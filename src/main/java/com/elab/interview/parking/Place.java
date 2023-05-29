package com.elab.interview.parking;

public class Place {
    private final int poszionInParking;
    private Character info;
    private int distancePedestrianExit = 0;

    public Place(int poszionInParking, Character info) {
        this.poszionInParking = poszionInParking;
        this.info = info;
    }

    // METHODS
    boolean isPedestrianExit() {
        return info.equals('=');
    }

    boolean isFreeDisabledBay(){
        return info.equals('@');
    }

    boolean isFreeNormalBay(){
        return info.equals('U');
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
