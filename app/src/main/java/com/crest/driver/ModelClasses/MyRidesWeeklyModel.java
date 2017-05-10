package com.crest.driver.ModelClasses;

/**
 * Created by cresttwo on 4/1/2016.
 */
public class MyRidesWeeklyModel {

    String name,date, payment,payment_type,ride_id,user_v_image;

    public String getUser_v_image() {
        return user_v_image;
    }

    public void setUser_v_image(String user_v_image) {
        this.user_v_image = user_v_image;
    }

    public MyRidesWeeklyModel(String ride_id, String name, String date, String payment, String payment_type, String user_v_image) {
        this.name = name;
        this.date = date;
        this.payment = payment;
        this.payment_type = payment_type;
        this.ride_id = ride_id;
        this.user_v_image = user_v_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }
}
