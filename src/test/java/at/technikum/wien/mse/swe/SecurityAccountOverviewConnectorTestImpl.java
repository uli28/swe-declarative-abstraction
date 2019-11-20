package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.connector.Alignment;
import at.technikum.wien.mse.swe.connector.Connector;
import at.technikum.wien.mse.swe.connector.DataField;
import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;
import at.technikum.wien.mse.swe.model.Amount;
import at.technikum.wien.mse.swe.model.DepotOwner;
import at.technikum.wien.mse.swe.model.RiskCategory;
import at.technikum.wien.mse.swe.model.SecurityAccountOverview;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author MatthiasKreuzriegler
 */
@Connector
public class SecurityAccountOverviewConnectorTestImpl implements
        SecurityAccountOverviewConnector {

    private static final int RISKCATEGORY_START_INDEX = 50;
    private static final int RISKCATEGORY_LENGTH = 2;
    private static final int LASTNAME_START_INDEX = 52;
    private static final int LASTNAME_LENGTH = 30;
    private static final int FIRSTNAME_START_INDEX = 82;
    private static final int FIRSTNAME_LENGTH = 30;
    private static final int CURRENCY_START_INDEX = 112;
    private static final int CURRENCY_LENGTH = 3;
    private static final int BALANCE_START_INDEX = 115;
    private static final int BALANCE_LENGTH = 17;

    @DataField(position = "40", length = 10, padding = "0", alignment = Alignment.LEFT)
    private String accountNumber;

    @Override
    public SecurityAccountOverview read(Path file) {
        String content;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            content = reader.readLine();
        } catch (IOException e) {
            throw new SecurityAccountOverviewReadException(e);
        }
        return mapOverview(content);
    }

    private SecurityAccountOverview mapOverview(String content) {
        SecurityAccountOverview overview = new SecurityAccountOverview();
//        overview.setAccountNumber(stripStart(
//                extract(content, ACCOUNTNUMBER_START_INDEX, ACCOUNTNUMBER_LENGTH),
//                ACCOUNTNUMBER_PADDING_CHAR));
        overview.setAccountNumber(accountNumber);
        overview.setRiskCategory(RiskCategory.fromCode(
                extract(content, RISKCATEGORY_START_INDEX, RISKCATEGORY_LENGTH).trim())
                .orElseThrow(IllegalStateException::new));
        overview.setDepotOwner(getDepotOwner(content));
        String currency = extract(content, CURRENCY_START_INDEX, CURRENCY_LENGTH).trim();
        BigDecimal balanceValue = BigDecimal.valueOf(
                Double.valueOf(extract(content, BALANCE_START_INDEX, BALANCE_LENGTH)));
        overview.setBalance(new Amount(currency, balanceValue));
        return overview;
    }

    private DepotOwner getDepotOwner(String content) {
        DepotOwner owner = new DepotOwner();
        owner.setLastname(extract(content, LASTNAME_START_INDEX, LASTNAME_LENGTH).trim());
        owner.setFirstname(extract(content, FIRSTNAME_START_INDEX, FIRSTNAME_LENGTH).trim());
        return owner;
    }

    private String extract(String content, int startIndex, int length) {
        return content.substring(startIndex, startIndex + length);
    }

}
