package com.unir.ata;

public class InstrumentCardItem {

    private int imageResourceId;
    private String text;
    private String description;

    public InstrumentCardItem(int imageResourceId, String text, String description) {
        this.imageResourceId = imageResourceId;
        this.text = text;
        this.description = description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getText() {
        return text;
    }
    public String getDescription() {
        return description;
    }
}

