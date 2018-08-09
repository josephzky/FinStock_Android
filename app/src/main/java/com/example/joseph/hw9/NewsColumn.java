package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/26/17.
 */

public class NewsColumn {
    private String title;
    private String author;
    private String date;
    private String link;

    public NewsColumn(String title, String author, String date, String link) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {this.link = link; }
}
