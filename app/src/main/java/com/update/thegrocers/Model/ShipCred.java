package com.update.thegrocers.Model;

public class ShipCred {
    private String email,password;

    public ShipCred(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ShipCred() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
