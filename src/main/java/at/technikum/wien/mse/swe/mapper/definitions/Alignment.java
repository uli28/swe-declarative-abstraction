package at.technikum.wien.mse.swe.mapper.definitions;

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
