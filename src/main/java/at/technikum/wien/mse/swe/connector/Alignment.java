package at.technikum.wien.mse.swe.connector;

import java.util.Arrays;
import java.util.Optional;

public enum Alignment {
    LEFT("left"),
    RIGHT("right");

    private final String alignment;

    Alignment(String alignment) {
        this.alignment = alignment;
    }

    @Override
    public String toString() {
        return this.alignment;
    }
}
