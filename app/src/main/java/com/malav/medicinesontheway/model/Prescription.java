package com.malav.medicinesontheway.model;

/**
 * Created by shahmalav on 03/08/17.
 */

public class Prescription {
    private String user_id;
    private String pres_id;
    private String pres_url;
    private String pres_name;


    public String getPres_name() {
        return pres_name;
    }

    public void setPres_name(String pres_name) {
        this.pres_name = pres_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPres_id() {
        return pres_id;
    }

    public void setPres_id(String pres_id) {
        this.pres_id = pres_id;
    }

    public String getPres_url() {
        return pres_url;
    }

    public void setPres_url(String pres_url) {
        this.pres_url = pres_url;
    }
}
