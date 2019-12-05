package at.technikum.wien.mse.swe.model;

import at.technikum.wien.mse.swe.mapper.definitions.Alignment;
import at.technikum.wien.mse.swe.mapper.definitions.ComplexField;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.mapper.definitions.Mapper;

/**
 * @author MatthiasKreuzriegler
 */
@Mapper
public class SecurityConfiguration {

    @DataField(name = "value", startIndex = 40, length = 12, padding = "", alignment = Alignment.LEFT)
    private ISIN isin;

    @DataField(startIndex = 52, length = 2, padding = "", alignment = Alignment.LEFT)
    private RiskCategory riskCategory;

    @DataField(startIndex = 54, length = 30, padding = "", alignment = Alignment.LEFT)
    private String name;

    @ComplexField(fieldMapper =
            {
                    @DataField(name = "currency", startIndex = 84, length = 3, padding = "", alignment = Alignment.LEFT),
                    @DataField(name = "value", startIndex = 87, length = 10, padding = "", alignment = Alignment.LEFT)
            }
    )
    private Amount yearHighest;

    @ComplexField(fieldMapper =
            {
                    @DataField(name = "currency", startIndex = 84, length = 3, padding = "", alignment = Alignment.LEFT),
                    @DataField(name = "value", startIndex = 97, length = 10, padding = "", alignment = Alignment.LEFT)
            }
    )
    private Amount yearLowest;

    public ISIN getIsin() {
        return isin;
    }

    public void setIsin(ISIN isin) {
        this.isin = isin;
    }

    public RiskCategory getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(RiskCategory riskCategory) {
        this.riskCategory = riskCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getYearHighest() {
        return yearHighest;
    }

    public void setYearHighest(Amount yearHighest) {
        this.yearHighest = yearHighest;
    }

    public Amount getYearLowest() {
        return yearLowest;
    }

    public void setYearLowest(Amount yearLowest) {
        this.yearLowest = yearLowest;
    }
}
