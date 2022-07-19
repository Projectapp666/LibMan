package com.example.myapplication;


import java.util.Map;

public class UserHelper {
    String address;
    String email;
    String name;
    Long pNo;
    String DOB;

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public UserHelper(String address, String email, String name, Long pNo, String DOB, boolean admin) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.pNo = pNo;
        this.DOB=DOB;


        this.admin = admin;
    }

    boolean admin;

    public UserHelper() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getpNo() {
        return pNo;
    }

    public void setpNo(Long pNo) {
        this.pNo = pNo;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}


