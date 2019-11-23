package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.connector.ConnectorProcessor;
import at.technikum.wien.mse.swe.connector.SecurityConfigurationConnectorImpl;
import at.technikum.wien.mse.swe.model.RiskCategory;
import at.technikum.wien.mse.swe.model.SecurityConfiguration;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityConfigurationConnectorRefactoredTest {

    private final SecurityConfigurationConnector sut = new SecurityConfigurationConnectorImpl();
    private static final String FILENAME = "examples/SecurityConfiguration_AT0000937503.txt";

    @Test
    public void testRead_notNull() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("configuration not found", configuration);
    }

    @Test
    public void testRead_isin() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("isin not found", configuration.getIsin());
        assertEquals("AT0000937503", configuration.getIsin().getValue());
    }

    @Test
    public void testRead_RiskCatedory() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertEquals(RiskCategory.LOW, configuration.getRiskCategory());
    }

    @Test
    public void testRead_Name() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertEquals("voestalpine Aktie", configuration.getName());
    }

    @Test
    public void testRead_YearHighest() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("yearHighest not found", configuration.getYearHighest());
        assertEquals("EUR", configuration.getYearHighest().getCurrency());
        assertEquals(BigDecimal.valueOf(54.98d), configuration.getYearHighest().getValue());
    }

    @Test
    public void testRead_YearLowest() throws URISyntaxException {
        SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("yearLowest not found", configuration.getYearLowest());
        assertEquals("EUR", configuration.getYearLowest().getCurrency());
        assertEquals(BigDecimal.valueOf(29.6d), configuration.getYearLowest().getValue());
    }

    private SecurityConfiguration createSecurityAccountOverview() throws URISyntaxException {
        SecurityConfigurationConnectorTestImpl connector = new SecurityConfigurationConnectorTestImpl();
        ConnectorProcessor connectorProcessor = new ConnectorProcessor();
        return (SecurityConfiguration) connectorProcessor.convertFileToModel(connector, Paths.get(ClassLoader.getSystemResource(FILENAME).toURI()));
    }

}
