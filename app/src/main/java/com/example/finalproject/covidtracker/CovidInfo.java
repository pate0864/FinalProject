package com.example.finalproject.covidtracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CovidInfo implements Serializable {
    String date;
    int changeCases;
    int changeFatalities;
    int changeHospitalization;
    int changeCritical;
    int changeRecoveries;
    int changeVaccinations;
    int totalCases;
    int totalFatalities;
    int totalHospitalization;
    int totalCritical;
    int totalRecoveries;
    int totalVaccinations;


    /*
    Using Constructor and it will return all the required details
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getChangeCases() {
        return changeCases;
    }

    public void setChangeCases(int changeCases) {
        this.changeCases = changeCases;
    }

    public int getChangeFatalities() {
        return changeFatalities;
    }

    public void setChangeFatalities(int changeFatalities) {
        this.changeFatalities = changeFatalities;
    }

    public int getChangeHospitalization() {
        return changeHospitalization;
    }

    public void setChangeHospitalization(int changeHospitalization) {
        this.changeHospitalization = changeHospitalization;
    }

    public int getChangeCritical() {
        return changeCritical;
    }

    public void setChangeCritical(int changeCritical) {
        this.changeCritical = changeCritical;
    }

    public int getChangeRecoveries() {
        return changeRecoveries;
    }

    public void setChangeRecoveries(int changeRecoveries) {
        this.changeRecoveries = changeRecoveries;
    }

    public int getChangeVaccinations() {
        return changeVaccinations;
    }

    public void setChangeVaccinations(int changeVaccinations) {
        this.changeVaccinations = changeVaccinations;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getTotalFatalities() {
        return totalFatalities;
    }

    public void setTotalFatalities(int totalFatalities) {
        this.totalFatalities = totalFatalities;
    }

    public int getTotalHospitalization() {
        return totalHospitalization;
    }

    public void setTotalHospitalization(int totalHospitalization) {
        this.totalHospitalization = totalHospitalization;
    }

    public int getTotalCritical() {
        return totalCritical;
    }

    public void setTotalCritical(int totalCritical) {
        this.totalCritical = totalCritical;
    }

    public int getTotalRecoveries() {
        return totalRecoveries;
    }

    public void setTotalRecoveries(int totalRecoveries) {
        this.totalRecoveries = totalRecoveries;
    }

    public int getTotalVaccinations() {
        return totalVaccinations;
    }

    public void setTotalVaccinations(int totalVaccinations) {
        this.totalVaccinations = totalVaccinations;
    }

    public void fromJSON(JSONObject json){
        try {
            date = json.getString("date");
            changeCases = json.getInt("change_cases");
            changeFatalities= json.getInt("change_fatalities");
            changeHospitalization= json.getInt("change_hospitalizations");
            changeCritical= json.getInt("change_criticals");
            changeRecoveries= json.getInt("change_recoveries");
            changeVaccinations= json.getInt("change_vaccinations");
            totalCases= json.getInt("total_cases");
            totalFatalities= json.getInt("total_fatalities");
            totalHospitalization= json.getInt("total_hospitalizations");
            totalCritical= json.getInt("total_criticals");
            totalRecoveries= json.getInt("total_recoveries");
            totalVaccinations= json.getInt("total_vaccinations");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
