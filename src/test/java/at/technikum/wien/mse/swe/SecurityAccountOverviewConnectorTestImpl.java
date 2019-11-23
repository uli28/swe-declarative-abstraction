package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.connector.Alignment;
import at.technikum.wien.mse.swe.connector.ComplexField;
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

    @DataField(startIndex = 40, length = 10, padding = "0", alignment = Alignment.LEFT)
    private String accountNumber;

    @DataField(startIndex = 50, length = 2, padding = "", alignment = Alignment.LEFT)
    private RiskCategory riskCategory;

    @ComplexField(fieldMapper =
            {
                    @DataField(name = "lastname", startIndex = 52, length = 30, padding = "", alignment = Alignment.RIGHT),
                    @DataField(name = "firstname", startIndex = 82, length = 30, padding = "", alignment = Alignment.RIGHT)
             }
    )
    private DepotOwner depotOwner;

    @ComplexField(fieldMapper =
            {
                    @DataField(name = "currency", startIndex = 112, length = 3, padding = "", alignment = Alignment.LEFT),
                    @DataField(name = "value", startIndex = 115, length = 17, padding = "", alignment = Alignment.LEFT)
            }
    )
    private Amount amount;

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
        return overview;
    }
}
