package com.crest.driver.ModelClasses;

/**
 * Created by brittany on 3/30/17.
 */

public class MyEarningsModel  {
    public MyEarningsModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    String title,date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
