package com.bkimp.life.model;
//public class Disease {
//    public String getDescription() {
//        return Description;
//    }
//
//    public void setDescription(String description) {
//        this.Description = description;
//    }
//
//    private String Description;
//    private Organic OrganicTreatment;
//    private Conventional ConventionalTreatment;
//    private Biological BiologicalTreatment;
//
//    // Getter for organicTreatment
//    public Organic getOrganicTreatment() {
//        return OrganicTreatment;
//    }
//
//    // Getter for conventionalTreatment
//    public Conventional getConventionalTreatment() {
//        return ConventionalTreatment;
//    }
//
//    // Getter for biologicalTreatment
//    public Biological getBiologicalTreatment() {
//        return BiologicalTreatment;
//    }
//
//    public static class Organic {
//        private String title;
//        private String shortContent;
//        private String longContent;
//
//        // Getter methods for title, shortContent, and longContent
//        public String getTitle() {
//            return title;
//        }
//
//        public String getShortContent() {
//            return shortContent;
//        }
//
//        public String getLongContent() {
//            return longContent;
//        }
//    }
//
//    public static class Conventional {
//        private String title;
//        private String shortContent;
//        private String longContent;
//
//        // Getter methods for title, shortContent, and longContent
//        public String getTitle() {
//            return title;
//        }
//
//        public String getShortContent() {
//            return shortContent;
//        }
//
//        public String getLongContent() {
//            return longContent;
//        }
//    }
//
//    public static class Biological {
//        private String title;
//        private String shortContent;
//        private String longContent;
//
//        // Getter methods for title, shortContent, and longContent
//        public String getTitle() {
//            return title;
//        }
//
//        public String getShortContent() {
//            return shortContent;
//        }
//
//        public String getLongContent() {
//            return longContent;
//        }
//    }
//}
//
public class Disease {
    private String Description;
    private Treatment OrganicTreatment;
    private Treatment ConventionalTreatment;
    private Treatment BiologicalTreatment;

    public String getDescription() {
        return Description;
    }

    public Treatment getOrganicTreatment() {
        return OrganicTreatment;
    }

    public Treatment getConventionalTreatment() {
        return ConventionalTreatment;
    }

    public Treatment getBiologicalTreatment() {
        return BiologicalTreatment;
    }
}
