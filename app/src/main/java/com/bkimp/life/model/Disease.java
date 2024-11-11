package com.bkimp.life.model;
public class Disease {
    private String Description;
    private Treatment OrganicTreatment;
    private Treatment ConventionalTreatment;
    private Treatment BiologicalTreatment;

    // Getters and setters
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Treatment getOrganicTreatment() {
        return OrganicTreatment;
    }

    public void setOrganicTreatment(Treatment organicTreatment) {
        this.OrganicTreatment = organicTreatment;
    }

    public Treatment getConventionalTreatment() {
        return ConventionalTreatment;
    }

    public void setConventionalTreatment(Treatment conventionalTreatment) {
        this.ConventionalTreatment = conventionalTreatment;
    }

    public Treatment getBiologicalTreatment() {
        return BiologicalTreatment;
    }

    public void setBiologicalTreatment(Treatment biologicalTreatment) {
        this.BiologicalTreatment = biologicalTreatment;
    }
}

