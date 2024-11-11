package com.bkimp.life.model;


import java.util.List;

public class DataWrapper {
    private int versions;
    private List<Category> Treatments;

    public DataWrapper(int versions, List<Category> Treatments) {
        this.versions = versions;
        this.Treatments = Treatments;
    }

    public int getVersions() {
        return versions;
    }

    public List<Category> getTreatments() {
        return Treatments;
    }
}
