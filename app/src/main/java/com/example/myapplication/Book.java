package com.example.myapplication;

public class Book {
    private String Id;
    private String title;
    private String Author;
    private String imageUrl;
    private String qrUrl;
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getBookLoc() {
        return bookLoc;
    }

    public void setBookLoc(String bookLoc) {
        this.bookLoc = bookLoc;
    }

    private String bookLoc;
    private int borrowcount;
    boolean available;

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    private String bookDesc;

    public Book() {
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getBorrowcount() {
        return borrowcount;
    }

    public void setBorrowcount(int borrowcount) {
        this.borrowcount = borrowcount;
    }

    public Book(String title, String author, String id, int borrowcount, String imageUrl, boolean available, String bookDesc, String date) {
        this.title = title;
        Author = author;
        Id = id;
        this.borrowcount = borrowcount;
        this.imageUrl = imageUrl;
        this.available=available;
        this.bookDesc=bookDesc;
        this.date=date;
    }

    public Book(String title, String author, String id, int borrowcount, String imageUrl, boolean available,String bookDesc) {
        this.title = title;
        Author = author;
        Id = id;
        this.borrowcount = borrowcount;
        this.imageUrl = imageUrl;
        this.available=available;
        this.bookDesc=bookDesc;
    }
    public Book(String title, String author, String id, int borrowcount,String qrUrl, String imageUrl, boolean available,String bookDesc,String date,String bookLoc) {
        this.title = title;
        Author = author;
        Id = id;
        this.borrowcount = borrowcount;
        this.imageUrl = imageUrl;
        this.available=available;
        this.bookDesc=bookDesc;
        this.qrUrl=qrUrl;
        this.date=date;
        this.bookLoc=bookLoc;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}