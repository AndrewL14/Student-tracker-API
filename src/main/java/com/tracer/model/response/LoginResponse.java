package com.test.practice.entity;

public class LoginResponse{
    private ApplicationUser user;
    private String jwt;

    public LoginResponse() {
    }

    public LoginResponse(ApplicationUser user , String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
