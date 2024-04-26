package com.unir.ata;

public class InstrumentCardItem {

    private int imageResourceId;
    private String text;
    private String description;
    private String tooltip;

    public InstrumentCardItem(int imageResourceId, String text, String description, String tooltip) {
        this.imageResourceId = imageResourceId;
        this.text = text;
        this.description = description;
        this.tooltip = tooltip;
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


    public String getTooltip() {
        return tooltip;
    }
}

