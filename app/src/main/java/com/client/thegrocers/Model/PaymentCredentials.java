package com.client.thegrocers.Model;

public class PaymentCredentials {
    private String loginId;
    private String password ;

    public PaymentCredentials(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public PaymentCredentials() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
