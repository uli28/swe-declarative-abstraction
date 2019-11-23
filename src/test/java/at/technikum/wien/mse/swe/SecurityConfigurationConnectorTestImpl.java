package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.connector.Alignment;
import at.technikum.wien.mse.swe.connector.ComplexField;
import at.technikum.wien.mse.swe.connector.Connector;
import at.technikum.wien.mse.swe.connector.DataField;
import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;
import at.technikum.wien.mse.swe.model.Amount;
import at.technikum.wien.mse.swe.model.ISIN;
import at.technikum.wien.mse.swe.model.RiskCategory;
import at.technikum.wien.mse.swe.model.SecurityConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author MatthiasKreuzriegler
 */
@Connector
public class SecurityConfigurationConnectorTestImpl implements SecurityConfigurationConnector {

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

    @Override
    public SecurityConfiguration read(Path file) {
        String content = readFileContent(file);
        return mapConficuration(content);
    }

    private SecurityConfiguration mapConficuration(String content) {
        SecurityConfiguration configuration = new SecurityConfiguration();
        return configuration;
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
}
