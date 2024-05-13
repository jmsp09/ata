package com.unir.ata;

import android.util.Log;

import java.util.HashSet;
import java.util.LinkedList;

public class LastDetections {

    private static final LinkedList<DetectedNote> lastItems = new LinkedList<>();
    private static int numEqualNotes = 1;
    protected static int MIN_EQUALS_NOTES = 5;
    private static int MAX_EQUALS_NOTES = MIN_EQUALS_NOTES * 3;
    private static final int MAX_ITEMS = 10;
    protected static final int NOTES = 0;
    protected static final int FREQUENCIES = 1;

    public static void addDetection(DetectedNote item) {

        if (!lastItems.isEmpty()){
            if (MAX_EQUALS_NOTES != numEqualNotes
                    && lastItems.getLast().getName().equals(item.getName())) {
                numEqualNotes++;
            } else {
                numEqualNotes = 1;
            }
        }
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

    public static int getNumEqualNotes() {
        return numEqualNotes;
    }
    
}

