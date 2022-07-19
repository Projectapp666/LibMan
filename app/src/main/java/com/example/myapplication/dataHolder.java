package com.example.myapplication;

public class dataHolder {
    private final boolean available;
    private int borrowCount;

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public boolean isAvailable() {
        return available;
    }

    String id, bookName, bookAuthor;


    public dataHolder(String id, String bookName, String bookAuthor, boolean available, int borrowCount) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.available = available;
        this.borrowCount=borrowCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
