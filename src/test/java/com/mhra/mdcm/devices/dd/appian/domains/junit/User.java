package com.mhra.mdcm.devices.dd.appian.domains.junit;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
public class User {

    private String initials;
    private String userName;
    private String password;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, String initials) {
        this.userName = userName;
        this.password = password;
        this.initials = initials;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getInitials() {
        return initials;
    }

    public void updateUsernamePassword(String userName, String newPassword) {
        this.userName = userName;
        this.password = newPassword;
    }


    @Override
    public String toString() {
        return "Test for : " + userName + "/" + password;
    }

}
