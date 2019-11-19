package at.technikum.wien.mse.swe.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author MatthiasKreuzriegler
 */
public enum RiskCategory {

    NON_EXISTING("00"),
    EXECUTION_ONLY("01"),
    LOW("02"),
    MIDDLE("04"),
    HIGH("06"),
    SPECULATIVE("08");

    private final String code;

    RiskCategory(String code) {
        this.code = code;
    }

    public static final Optional<RiskCategory> fromCode(String code){
        return Arrays.stream(values()).filter(rc -> rc.code.equalsIgnoreCase(code)).findFirst();
    }
}
