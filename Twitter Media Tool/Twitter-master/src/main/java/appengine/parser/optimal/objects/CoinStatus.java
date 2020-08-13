package appengine.parser.optimal.objects;

public class CoinStatus {

    private String coinName;
    private String label;
    private boolean isWalletActive;
    private boolean isListingActive;
    private Market market;


    public CoinStatus(Market market, String coinName, String label, boolean isWalletActive, boolean isListingActive) {
        this.market = market;
        this.coinName = coinName;
        this.label = label;
        this.isWalletActive = isWalletActive;
        this.isListingActive = isListingActive;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getLabel() {
        return label;
    }

    public boolean isWalletActive() {
        return isWalletActive;
    }

    public boolean isListingActive() {
        return isListingActive;
    }


    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

}
