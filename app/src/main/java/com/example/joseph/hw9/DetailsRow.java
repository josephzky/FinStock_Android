package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/25/17.
 */

public class DetailsRow {
    private String title;
    private String value;

    public DetailsRow(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
