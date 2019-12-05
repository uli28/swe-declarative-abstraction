package at.technikum.wien.mse.swe.model;

import at.technikum.wien.mse.swe.mapper.definitions.Alignment;
import at.technikum.wien.mse.swe.mapper.definitions.ComplexField;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.mapper.definitions.Mapper;

/**
 * @author MatthiasKreuzriegler
 */
@Mapper
public class SecurityAccountOverview {

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
    private Amount balance;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public RiskCategory getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(RiskCategory riskCategory) {
        this.riskCategory = riskCategory;
    }

    public DepotOwner getDepotOwner() {
        return depotOwner;
    }

    public void setDepotOwner(DepotOwner depotOwner) {
        this.depotOwner = depotOwner;
    }


    @Override
    public String toString() {
        return "SecurityAccountOverview{" +
                "accountNumber='" + accountNumber + '\'' +
                ", riskCategory=" + riskCategory +
                ", depotOwner=" + depotOwner +
                ", balance=" + balance +
                '}';
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public Amount getBalance() {
        return balance;
    }
}
