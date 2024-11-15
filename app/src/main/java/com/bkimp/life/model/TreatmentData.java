package com.bkimp.life.model;

import java.util.List;

public class TreatmentData {
    private int versions;
    private List<Treatment> Treatments;

    public int getVersions() {
        return versions;
    }

    public void setVersions(int versions) {
        this.versions = versions;
    }

    public List<Treatment> getTreatments() {
        return Treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        Treatments = treatments;
    }
}
