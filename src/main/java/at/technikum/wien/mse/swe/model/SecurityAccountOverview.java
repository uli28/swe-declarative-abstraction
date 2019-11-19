package at.technikum.wien.mse.swe.model;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityAccountOverview {

    private String accountNumber;
    private RiskCategory riskCategory;
    private DepotOwner depotOwner;
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

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
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
}
