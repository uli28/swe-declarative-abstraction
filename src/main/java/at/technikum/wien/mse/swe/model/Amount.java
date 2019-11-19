package at.technikum.wien.mse.swe.model;

import java.math.BigDecimal;

/**
 * @author MatthiasKreuzriegler
 */
public class Amount {

    private String currency;
    private BigDecimal value;

    public Amount(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
