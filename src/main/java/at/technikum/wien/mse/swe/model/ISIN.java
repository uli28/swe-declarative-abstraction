package at.technikum.wien.mse.swe.model;

/**
 * @author MatthiasKreuzriegler
 */
public class ISIN {

    private final String value;

    public ISIN(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
