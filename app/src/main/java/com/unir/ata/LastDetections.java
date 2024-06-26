package com.unir.ata;

import java.util.LinkedList;

public class LastDetections {

    private static LinkedList<DetectedNote> lastItems = new LinkedList<>();
    private static int numEqualNotes = 1;
    protected static final int MIN_EQUALS_NOTES = 4;
    protected static final int MIN_EQUALS_NOTES_TO_PRINT = 1 ;
    private static final int MAX_EQUALS_NOTES = MIN_EQUALS_NOTES * 3;
    private static final int MAX_ITEMS = 10;

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

    protected static void reset() {
        numEqualNotes = 1;
    }
    
}

