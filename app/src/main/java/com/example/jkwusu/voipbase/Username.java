package com.example.jkwusu.voipbase;

import android.app.Application;

/**
 * Created by jkwusu on 2016/1/18.
 */
public class Username extends Application {
    public static String username="";
    public static String balance="";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
