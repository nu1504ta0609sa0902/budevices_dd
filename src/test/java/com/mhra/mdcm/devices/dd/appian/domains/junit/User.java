package com.mhra.mdcm.devices.dd.appian.domains.junit;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
public class User {

    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Test for : " + userName + "/" + password;
    }
}
