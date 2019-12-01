package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.creators.impl.DefaultConnectorFactory;
import at.technikum.wien.mse.swe.mapper.SecurityAccountOverviewMapper;
import at.technikum.wien.mse.swe.model.SecurityAccountOverview;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static at.technikum.wien.mse.swe.model.RiskCategory.NON_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the creation of the SecurityAccountOverview model with the help ot the created mapper object
 *
 * @author Ulrich Gram
 */
public class SecurityAccountOverviewConnectorTest {

    private static final String FILENAME = "examples/SecurityAccountOverview_12345678.txt";

    @Test
    public void testRead_notNull() throws URISyntaxException {
        final SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("overview not found", overview);
    }

    @Test
    public void testRead_accountNumber() throws URISyntaxException {
        final SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("accountNumber not found", overview.getAccountNumber());
        assertEquals("12345678", overview.getAccountNumber());
    }

    @Test
    public void testRead_riskCategory() throws URISyntaxException {
        final SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("riskCategory not found", overview.getRiskCategory());
        assertEquals(NON_EXISTING, overview.getRiskCategory());
    }

    @Test
    public void testRead_DepotOwner() throws URISyntaxException {
        final SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("depotOwner not found", overview.getDepotOwner());
        assertEquals("MUSTERMANN", overview.getDepotOwner().getLastname());
        assertEquals("MAX UND MARIA", overview.getDepotOwner().getFirstname());
    }

    @Test
    public void testRead_Balance() throws URISyntaxException {
        final SecurityAccountOverview overview = createSecurityAccountOverview();
        assertNotNull("balance not found", overview.getBalance());
        assertEquals("EUR", overview.getBalance().getCurrency());
        assertEquals(BigDecimal.valueOf(1692.45), overview.getBalance().getValue());
    }

    private SecurityAccountOverview createSecurityAccountOverview() throws URISyntaxException {
        return new DefaultConnectorFactory<SecurityAccountOverview>
                (SecurityAccountOverviewMapper.class, SecurityAccountOverview.class)
                .read(Paths.get(ClassLoader.getSystemResource(FILENAME).toURI()));
    }
}
