package com.example.finalproject.carbondioxideinterface;

import java.io.Serializable;

public class CarbonEmissionEstimate implements Serializable {

    String estimateId;
    String vehicleMaker;
    String vehicleModel;
    String vehicleYear;
    double distance;
    String distanceUnit;
    double carbonInG;
    double carbonInLb;
    double carbonInMt;
    double carbonInKg;

    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public double getCarbonInG() {
        return carbonInG;
    }

    public void setCarbonInG(double carbonInG) {
        this.carbonInG = carbonInG;
    }

    public double getCarbonInLb() {
        return carbonInLb;
    }

    public void setCarbonInLb(double carbonInLb) {
        this.carbonInLb = carbonInLb;
    }

    public double getCarbonInMt() {
        return carbonInMt;
    }

    public void setCarbonInMt(double carbonInMt) {
        this.carbonInMt = carbonInMt;
    }

    public double getCarbonInKg() {
        return carbonInKg;
    }

    public void setCarbonInKg(double carbonInKg) {
        this.carbonInKg = carbonInKg;
    }
}
