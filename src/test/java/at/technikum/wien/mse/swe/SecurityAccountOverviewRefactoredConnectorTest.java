package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.connector.ConnectorProcessor;
import at.technikum.wien.mse.swe.connector.SecurityAccountOverviewConnectorImpl;
import at.technikum.wien.mse.swe.model.SecurityAccountOverview;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static at.technikum.wien.mse.swe.model.RiskCategory.NON_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityAccountOverviewRefactoredConnectorTest {

    private final SecurityAccountOverviewConnector sut = new SecurityAccountOverviewConnectorImpl();
    private static final String FILENAME = "examples/SecurityAccountOverview_12345678.txt";


    @Test
    public void testRead_notNull() throws URISyntaxException {
        SecurityAccountOverview overview = sut.read(Paths.get(ClassLoader.getSystemResource(FILENAME).toURI()));
        assertNotNull("overview not found", overview);
    }

    @Test
    public void testRead_accountNumber() throws URISyntaxException {
        SecurityAccountOverview returnObject = createSecurityAccountOverview();
        assertNotNull("accountNumber not found", returnObject.getAccountNumber());
        assertEquals("12345678", returnObject.getAccountNumber());
    }

    @Test
    public void testRead_riskCategory() throws URISyntaxException {
        SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("riskCategory not found", overview.getRiskCategory());
        assertEquals(NON_EXISTING, overview.getRiskCategory());
    }

    @Test
    public void testRead_DepotOwner() throws URISyntaxException {
        SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("depotOwner not found", overview.getDepotOwner());
        assertEquals("MUSTERMANN", overview.getDepotOwner().getLastname());
        assertEquals("MAX UND MARIA", overview.getDepotOwner().getFirstname());
    }

    @Test
    public void testRead_Balance() throws URISyntaxException {
        SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("balance not found", overview.getBalance());
        assertEquals("EUR", overview.getBalance().getCurrency());
        assertEquals(BigDecimal.valueOf(1692.45), overview.getBalance().getValue());
    }

    private SecurityAccountOverview createSecurityAccountOverview() throws URISyntaxException {
        SecurityAccountOverviewConnector connector = new SecurityAccountOverviewConnectorTestImpl();
        ConnectorProcessor connectorProcessor = new ConnectorProcessor();
        return (SecurityAccountOverview) connectorProcessor.convertFileToModel(connector, Paths.get(ClassLoader.getSystemResource(FILENAME).toURI()));
    }
}