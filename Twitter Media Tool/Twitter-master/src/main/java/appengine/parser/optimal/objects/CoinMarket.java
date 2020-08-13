package appengine.parser.optimal.objects;

import appengine.parser.optimal.constants.MarketConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by anand.kurapati on 05/01/18.
 */
public class CoinMarket {

    private Market market;
    private String coinName;
    private Double buyprice;
    private Double sellprice;
    private Double totalvolume;
    private CoinType coinType = null;
    public Double profit;


    public CoinMarket(Market market, String coinName, BigDecimal buyprice, BigDecimal sellprice, BigDecimal totalvolume) {
        setMarket(market);
        setCoinName(coinName);
        setBuyPrice(buyprice.doubleValue());
        setSellPrice(sellprice.doubleValue());
        setTotalvolume(totalvolume.doubleValue());
    }

    public CoinMarket(Market market, String coinName, String buyprice, String sellprice, String totalvolume) {
        setMarket(market);
        setCoinName(coinName);
        setBuyPrice(new Double(new Double(buyprice)));
        setSellPrice(new Double(new Double(sellprice)));
        setTotalvolume(new Double(new Double(totalvolume)));
    }


    public void setMarket(Market market) {
        this.market = market;
    }

    public void setCoinName(String label) {
        if (label.contains("/")) {
            String[] pair = label.split("/");
            coinName = pair[0];
        } else if (label.contains("_")) {
            String[] pair = label.split("_");
            coinName = pair[0];
        } else {
            coinName = label;
        }
        coinName = coinName.toUpperCase();
    }

    public void setBuyPrice(Double buyprice) {
        this.buyprice = buyprice;
    }

    public void setSellPrice(Double sellprice) {
        this.sellprice = sellprice;
    }


    public void setTotalvolume(Double totalvolume) {
        this.totalvolume = totalvolume;
    }

    public Market getMarket() {
        return market;
    }

    public String getCoinName() {
        return coinName;
    }

    public Double getOurBuyPrice() {
        return buyprice;
    }

    public Double getOurSellPrice() {
        return sellprice;
    }

    public Double getTotalVolume() {
        return totalvolume;
    }

    public CoinType getCoinType() {
        if (coinType == null) {
            coinType = CoinType.MEDIUM;
            if (getTotalVolume().doubleValue() < 1) {
                if (isVariantByTimes(2)) {
                    return CoinType.SPAM;
                }
            } else if (isVariantByTimes(1)) {
                return CoinType.SPAM;
            }


            if (getTotalVolume().doubleValue() < 1) {
                if (isNotVariantByTimes(2)) {
                    return CoinType.MEDIUM;
                }
            }

            if (getTotalVolume().doubleValue() > 5 && getTotalVolume().doubleValue() < 10) {
                return CoinType.EMERGING;
            }

            if (getTotalVolume().doubleValue() > 10) {
                return CoinType.BRANDED;
            }
        }
        return coinType;
    }

    public boolean isVariantByTimes(int times) {
        return (buyprice.doubleValue() - sellprice.doubleValue()) > (buyprice.doubleValue() / times);
    }

    public boolean isNotVariantByTimes(int times) {
        return (buyprice.doubleValue() - sellprice.doubleValue()) < (buyprice.doubleValue() / times);
    }

    public boolean equalsMarket(String marketString) {
        String currentMarket = "";
        switch (this.market) {
            case CRYPTOPIA:
                currentMarket = MarketConstants.CrypropiaString;
                break;
            case POLONEIX:
                currentMarket = MarketConstants.PoloneixString;
                break;
            case HitBTC:
                currentMarket = MarketConstants.HitbtcString;
                break;
            case BINANCE:
                currentMarket = MarketConstants.BitzString;
                break;
            case LIVECOIN:
                currentMarket = MarketConstants.LivecoinString;
                break;
            case LIQUI:
                currentMarket = MarketConstants.LiquiString;
                break;
            case BITZ:
                currentMarket = MarketConstants.BitzString;
                break;
            case OKEX:
                currentMarket = MarketConstants.OkexString;
                break;
            case COBINHOOD:
                currentMarket = MarketConstants.CobinhoodString;
                break;
            case COINEXCHANGE:
                currentMarket = MarketConstants.CoinExchangeString;
                break;
            case SOUTHXCHANGE:
                currentMarket = MarketConstants.SouthXChangeString;
                break;
        }

        if (currentMarket.equals(marketString)) {
            return true;
        }

        return false;
    }


    @Override
    public String toString() {
        return "Market :" + getMarket() + " Coin :" + getCoinName() + " Buy :" + doubleFormatter(getOurBuyPrice()) +
                " Sell :" + doubleFormatter(getOurSellPrice()) +
                " Volume :" + getTotalVolume() + " CoinType :" + getCoinType();
    }

    private String doubleFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(value);
    }

}
