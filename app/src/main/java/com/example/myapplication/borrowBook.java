package com.example.myapplication;

import java.util.Map;

class borrowBook{
    String author;
    String title;
    Object date;

    public long getDate() {
        return Long.parseLong((String) date);
    }



    public void setDate(Object date) {
        this.date = date;
    }

    public borrowBook(Object date, String title, String author) {


        this.date = date;
        this.title =title;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    String uid;

    public borrowBook( String uid, Object date,String title,String author) {

        this.uid = uid;
        this.date = date;
        this.title =title;
        this.author = author;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }







    public borrowBook() {
    }


}