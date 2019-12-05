package at.technikum.wien.mse.swe;

import at.technikum.wien.mse.swe.creators.impl.DefaultConnectorFactory;
import at.technikum.wien.mse.swe.model.RiskCategory;
import at.technikum.wien.mse.swe.model.SecurityConfiguration;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the creation of the SecurityConfiguration model with the help ot the created mapper object
 *
 * @author Ulrich Gram
 */
public class SecurityConfigurationConnectorTest {

    private static final String FILENAME = "examples/SecurityConfiguration_AT0000937503.txt";

    @Test
    public void testRead_notNull() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("configuration not found", configuration);
    }

    @Test
    public void testRead_isin() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("isin not found", configuration.getIsin());
        assertEquals("AT0000937503", configuration.getIsin().getValue());
    }

    @Test
    public void testRead_RiskCatedory() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertEquals(RiskCategory.LOW, configuration.getRiskCategory());
    }

    @Test
    public void testRead_Name() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertEquals("voestalpine Aktie", configuration.getName());
    }

    @Test
    public void testRead_YearHighest() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("yearHighest not found", configuration.getYearHighest());
        assertEquals("EUR", configuration.getYearHighest().getCurrency());
        assertEquals(BigDecimal.valueOf(54.98d), configuration.getYearHighest().getValue());
    }

    @Test
    public void testRead_YearLowest() throws URISyntaxException {
        final SecurityConfiguration configuration = createSecurityAccountOverview();
        assertNotNull("yearLowest not found", configuration.getYearLowest());
        assertEquals("EUR", configuration.getYearLowest().getCurrency());
        assertEquals(BigDecimal.valueOf(29.6d), configuration.getYearLowest().getValue());
    }

    private SecurityConfiguration createSecurityAccountOverview() throws URISyntaxException {
        return new DefaultConnectorFactory<SecurityConfiguration>
                (SecurityConfiguration.class, SecurityConfiguration.class)
                .read(Paths.get(ClassLoader.getSystemResource(FILENAME).toURI()));
    }
}
