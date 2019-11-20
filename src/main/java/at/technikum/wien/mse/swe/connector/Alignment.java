package at.technikum.wien.mse.swe.connector;

import java.util.Arrays;
import java.util.Optional;

public enum Alignment {
    LEFT("left"),
    RIGHT("right");

    private final String alignment;

    private Alignment(String alignment) {
        this.alignment = alignment;
    }

    Optional<Alignment> fromAlignment(String alignment) {
        return Arrays.stream(values()).filter(al -> al.alignment.equalsIgnoreCase(alignment))
                .findFirst();
    }

    @Override
    public String toString() {
        return this.alignment;
    }
}
