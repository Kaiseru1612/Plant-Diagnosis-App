package com.bkimp.life.model;

import java.util.List;
import java.util.Map;

//public class Treatment {
//    private String category;  // Ensure this field exists
//    private List<TreatmentDetails> en; // Assuming this is an array of TreatmentDetails objects
//    private List<TreatmentDetails> vn;
//    private String iconName;
//    private String serviceCode;
//
//    public String getServiceCode() {
//        return serviceCode;
//    }
//
//    public void setServiceCode(String serviceCode) {
//        this.serviceCode = serviceCode;
//    }
//
//    public String getIconName() {
//        return iconName;
//    }
//
//    public void setIconName(String iconName) {
//        this.iconName = iconName;
//    }
//
//    public List<TreatmentDetails> getVn() {
//        return vn;
//    }
//
//    public void setVn(List<TreatmentDetails> vn) {
//        this.vn = vn;
//    }
//
//    public List<TreatmentDetails> getEn() {
//        return en;
//    }
//
//    public void setEn(List<TreatmentDetails> en) {
//        this.en = en;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//}


public class Treatment {
    private String title;
    private String shortContent;
    private String longContent;

    public String getTitle() {
        return title;
    }

    public String getShortContent() {
        return shortContent;
    }

    public String getLongContent() {
        return longContent;
    }
}