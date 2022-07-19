package com.example.myapplication;

public class LibraryPrefsModel {
    String LibraryName;
    String otime;
    String cTime;
    int borrowLimit;
    int borrowTimeLimitInDays;

    public LibraryPrefsModel(String libraryName, String oTime, String cTime, int borrowLimit, int borrowTimeLimitInDays) {
        LibraryName = libraryName;
        this.otime = oTime;
        this.cTime = cTime;
        this.borrowLimit = borrowLimit;
        this.borrowTimeLimitInDays = borrowTimeLimitInDays;
    }

    public LibraryPrefsModel() {
    }

    public String getLibraryName() {
        return LibraryName;
    }

    public void setLibraryName(String libraryName) {
        LibraryName = libraryName;
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public int getBorrowTimeLimitInDays() {
        return borrowTimeLimitInDays;
    }

    public void setBorrowTimeLimitInDays(int borrowTimeLimitInDays) {
        this.borrowTimeLimitInDays = borrowTimeLimitInDays;
    }
}
