package com.unir.ata;

import java.util.LinkedList;

public class LastDetections {

    private static final LinkedList<DetectedNote> lastItems = new LinkedList<>();
    private static final int MAX_ITEMS = 10;
    protected static final int NOTES = 0;
    protected static final int FREQUENCIES = 1;

    public static void addDetection(DetectedNote item) {
        lastItems.addLast(item);
        if (lastItems.size() > MAX_ITEMS) {
            lastItems.removeFirst();
        }
    }

    public static LinkedList<DetectedNote> getlastItems() {
        return lastItems;
    }

    public static String[] print() {
        StringBuilder notes = new StringBuilder();
        StringBuilder freqs = new StringBuilder();
        for (DetectedNote note : getlastItems()) {
            notes.append(note.getName()).append(";\n");
            freqs.append(note.getFrequency()).append("Hz;\n");
        }
        return new String[]{notes.toString(), freqs.toString()};
    }

    
}

