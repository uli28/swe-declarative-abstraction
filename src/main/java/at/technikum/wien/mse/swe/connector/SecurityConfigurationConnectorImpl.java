package at.technikum.wien.mse.swe.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import at.technikum.wien.mse.swe.SecurityConfigurationConnector;
import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;
import at.technikum.wien.mse.swe.model.Amount;
import at.technikum.wien.mse.swe.model.ISIN;
import at.technikum.wien.mse.swe.model.RiskCategory;
import at.technikum.wien.mse.swe.model.SecurityConfiguration;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityConfigurationConnectorImpl implements SecurityConfigurationConnector {

    private static final int ISIN_START_INDEX = 40;
    private static final int ISIN_LENGTH = 12;
    private static final int RISKCATEGORY_START_INDEX = 52;
    private static final int RISKCATEGORY_LENGTH = 2;
    private static final int NAME_START_INDEX = 54;
    private static final int NAME_LENGTH = 30;
    private static final int CURRENCY_START_INDEX = 84;
    private static final int CURRENCY_LENGTH = 3;
    private static final int HIGHEST_START_INDEX = 87;
    private static final int HIGHEST_LENGTH = 10;
    private static final int LOWEST_START_INDEX = 97;
    private static final int LOWEST_LENGTH = 10;

    @Override
    public SecurityConfiguration read(Path file) {
        String content = readFileContent(file);
        return mapConficuration(content);
    }

    private SecurityConfiguration mapConficuration(String content) {
        SecurityConfiguration configuration = new SecurityConfiguration();

        configuration.setIsin(getIsin(content));
        configuration.setRiskCategory(getRiskCategory(content));
        configuration.setName(getName(content));
        configuration.setYearHighest(getYearHighest(content));
        configuration.setYearLowest(getYearLowest(content));

        return configuration;
    }

    private Amount getYearHighest(String content) {
        String currency = getCurrency(content);
        BigDecimal balanceValue = getYearHighestValue(content);
        return new Amount(currency, balanceValue);
    }

    private Amount getYearLowest(String content) {
        String currency = getCurrency(content);
        BigDecimal balanceValue = getYearLowestValue(content);
        return new Amount(currency, balanceValue);
    }

    private BigDecimal getYearHighestValue(String content) {
        return BigDecimal.valueOf(
                Double.valueOf(extract(content, HIGHEST_START_INDEX, HIGHEST_LENGTH)));
    }

    private BigDecimal getYearLowestValue(String content) {
        return BigDecimal.valueOf(
                Double.valueOf(extract(content, LOWEST_START_INDEX, LOWEST_LENGTH)));
    }

    private String getCurrency(String content) {
        return extract(content, CURRENCY_START_INDEX, CURRENCY_LENGTH).trim();
    }

    private String getName(String content) {
        return extract(content, NAME_START_INDEX, NAME_LENGTH).trim();
    }

    private ISIN getIsin(String content) {
        String isinValue = extract(content, ISIN_START_INDEX, ISIN_LENGTH).trim();
        return new ISIN(isinValue);
    }

    private String readFileContent(Path file) {
        String content;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            content = reader.readLine();
        } catch (IOException e) {
            throw new SecurityAccountOverviewReadException(e);
        }
        return content;
    }

    private String extract(String content, int startIndex, int length) {
        return content.substring(startIndex, startIndex + length);
    }

    private RiskCategory getRiskCategory(String content) {
        return RiskCategory.fromCode(
                extract(content, RISKCATEGORY_START_INDEX, RISKCATEGORY_LENGTH).trim())
                .orElseThrow(IllegalStateException::new);
    }
}
