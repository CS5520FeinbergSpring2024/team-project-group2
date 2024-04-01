package edu.northeastern.recipeasy.domain;

public enum MessageType {
    SENT(1), RECEIVED(2);

    private final int value;
    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
