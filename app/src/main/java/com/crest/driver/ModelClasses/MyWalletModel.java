package com.crest.driver.ModelClasses;

/**
 * Created by brittany on 3/30/17.
 */

public class MyWalletModel {


    String title,from;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public MyWalletModel(String title, String from) {
        this.title = title;
        this.from = from;
    }
}
