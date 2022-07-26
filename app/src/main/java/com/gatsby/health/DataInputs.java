package com.gatsby.health;

public class DataInputs {
    String admin;
    String heart_beats;
    String blood_press;
    String o2_per;
    String date;

    public DataInputs(String admin, String blood_press, String o2_per, String heart_beats, String date){
        this.admin = admin;
        this.blood_press = blood_press;
        this.o2_per = o2_per;
        this.heart_beats = heart_beats;
        this.date = date;
    }
    public DataInputs(){}

    public String getAdmin() {
        return admin;
    }

    public String getHeart_beats() {
        return heart_beats;
    }

    public String getBlood_press() {
        return blood_press;
    }

    public String getO2_per() {
        return o2_per;
    }

    public String getDate() {
        return date;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setHeart_beats(String heart_beats) {
        this.heart_beats = heart_beats;
    }

    public void setBlood_press(String blood_press) {
        this.blood_press = blood_press;
    }

    public void setO2_per(String o2_per) {
        this.o2_per = o2_per;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
