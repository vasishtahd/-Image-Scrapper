package appengine.parser.optimal.objects;

import appengine.parser.optimal.constants.ExcludeList;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anand.kurapati on 14/01/18.
 */
public class ResultOfCalculation {

    private String coin;
    private CoinMarket lowestBuyCoin;
    private CoinMarket highestSellCoin;
    private List<CoinMarket> allOtherCoins;
    private Timestamp timestamp;
    private Double profit;

    public ResultOfCalculation(String coin, CoinMarket lowestBuyCoin, CoinMarket highesSellCoin, List<CoinMarket> allMarketsOfCoins) {
        this.coin = coin;
        this.lowestBuyCoin = lowestBuyCoin;
        this.highestSellCoin = highesSellCoin;
        allMarketsOfCoins.remove(highesSellCoin);
        allMarketsOfCoins.remove(lowestBuyCoin);
        this.allOtherCoins = allMarketsOfCoins;
        validateExclusionList();
    }

    private void validateExclusionList() {

        try {

            if (ExcludeList.isExcluded(coin, lowestBuyCoin, null)) {
                CoinMarket nextLowestBuyMarket = null;
                Double lowestBuyPrice = new Double(10);

                for (CoinMarket otherCoinMarket : allOtherCoins) {
                    if (otherCoinMarket.getOurBuyPrice() < lowestBuyPrice) {
                        if (!ExcludeList.isExcluded(coin, otherCoinMarket, null)) {
                            nextLowestBuyMarket = otherCoinMarket;
                            lowestBuyPrice = otherCoinMarket.getOurBuyPrice();
                        }
                    }
                }

                if (nextLowestBuyMarket != null) {
                    this.lowestBuyCoin = nextLowestBuyMarket;
                } else {
                    // means no good pair exists hence loss will occur if u buy
                    this.lowestBuyCoin.setBuyPrice(new Double(10));
                    return;
                }
            }

            if (ExcludeList.isExcluded(coin, highestSellCoin, null)) {
                CoinMarket nextHighestSellMarket = null;
                Double highestSellPrice = new Double(0);

                for (CoinMarket otherCoinMarket : allOtherCoins) {
                    if (otherCoinMarket.getOurSellPrice() > highestSellPrice) {
                        if (!ExcludeList.isExcluded(coin, otherCoinMarket, null)) {
                            nextHighestSellMarket = otherCoinMarket;
                            highestSellPrice = otherCoinMarket.getOurSellPrice();
                        }
                    }
                }

                if (nextHighestSellMarket != null) {
                    this.highestSellCoin = nextHighestSellMarket;
                } else {
                    // means no good pair hence loss will occur if u sell anywhere
                    this.highestSellCoin.setSellPrice(new Double(0));
                    return;
                }
            }

            if (ExcludeList.isExcluded(coin, lowestBuyCoin, highestSellCoin)) {

                CoinMarket nextLowestBuyMarket = null;
                Double lowestBuyPrice = new Double(10);

                for (CoinMarket otherCoinMarket : allOtherCoins) {
                    if (otherCoinMarket.getOurBuyPrice() < lowestBuyPrice) {
                        if (!ExcludeList.isExcluded(coin, otherCoinMarket, highestSellCoin)) {
                            nextLowestBuyMarket = otherCoinMarket;
                            lowestBuyPrice = otherCoinMarket.getOurBuyPrice();
                        }
                    }
                }

                if (nextLowestBuyMarket == null) {
                    // means no pair exists with current highestSellcoin , so try changing highestSellCoin

                    CoinMarket nextHighestSellMarket = null;
                    Double highestSellPrice = new Double(0);

                    for (CoinMarket otherCoinMarket : allOtherCoins) {
                        if (otherCoinMarket.getOurSellPrice() > highestSellPrice) {
                            if (!ExcludeList.isExcluded(coin, lowestBuyCoin, otherCoinMarket)) {
                                nextHighestSellMarket = otherCoinMarket;
                                highestSellPrice = otherCoinMarket.getOurSellPrice();
                            }
                        }
                    }

                    if (nextHighestSellMarket == null) {
                        //Ditch this dont sell this coin
                        this.highestSellCoin.setSellPrice(new Double(0));
                        this.lowestBuyCoin.setBuyPrice(new Double(10));
                        return;
                    } else {
                        this.highestSellCoin = nextHighestSellMarket;
                    }
                } else {

                    CoinMarket nextHighestSellMarket = null;
                    Double highestSellPrice = new Double(0);

                    for (CoinMarket otherCoinMarket : allOtherCoins) {
                        if (otherCoinMarket.getOurSellPrice() > highestSellPrice) {
                            if (!ExcludeList.isExcluded(coin, lowestBuyCoin, otherCoinMarket)) {
                                nextHighestSellMarket = otherCoinMarket;
                                highestSellPrice = otherCoinMarket.getOurSellPrice();
                            }
                        }
                    }

                    if (nextHighestSellMarket == null) {
                        //Ditch this dont sell this coin
                        this.highestSellCoin.setSellPrice(new Double(0));
                        this.lowestBuyCoin.setBuyPrice(new Double(10));
                        return;
                    }

                    Double lowestBuy = nextLowestBuyMarket.getOurBuyPrice();

                    Double higherSell = highestSellCoin.getOurSellPrice();

                    Double profitPercentageWithLowestBuyMarketChanged = ((higherSell - lowestBuy) / lowestBuy) * 100;

                    lowestBuy = lowestBuyCoin.getOurBuyPrice();

                    higherSell = nextHighestSellMarket.getOurSellPrice();

                    Double profitPercentageWithHighestSellMarketChanged = ((higherSell - lowestBuy) / lowestBuy) * 100;

                    if (profitPercentageWithHighestSellMarketChanged > profitPercentageWithLowestBuyMarketChanged) {
                        highestSellCoin = nextHighestSellMarket;
                    } else {
                        lowestBuyCoin = nextLowestBuyMarket;
                    }

                }

            }
        } catch (Exception e) {
            System.out.println(toJSON());
            e.printStackTrace();
            this.highestSellCoin.setSellPrice(new Double(0));
            this.lowestBuyCoin.setBuyPrice(new Double(10));
        }

    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimeStamp() {
        return this.timestamp;
    }

    public void setLowestBuyCoin(CoinMarket coinMarket) {
        this.lowestBuyCoin = coinMarket;
    }

    public void setHighestSellCoin(CoinMarket coinMarket) {
        this.highestSellCoin = coinMarket;
    }

    public CoinMarket getLowestBuyCoin() {
        return lowestBuyCoin;
    }

    public CoinMarket getHighestSellCoin() {
        return highestSellCoin;
    }

    public Double profitPercentage() {

        Double lowestBuy = lowestBuyCoin.getOurBuyPrice();

        Double higherSell = highestSellCoin.getOurSellPrice();

        Double profitPercentage = ((higherSell - lowestBuy) / lowestBuy) * 100;

        this.profit = profitPercentage;

        setProfitForOtherMarkets();

        return profitPercentage;
    }

    private Double profitPercentage(CoinMarket higherSellCoin) {

        Double lowestBuy = lowestBuyCoin.getOurBuyPrice();

        Double higherSell = higherSellCoin.getOurSellPrice();

        Double profitPercentage = ((higherSell - lowestBuy) / lowestBuy) * 100;

        return profitPercentage;
    }

    public List<CoinMarket> getAllOtherMarkets() {
        return allOtherCoins;
    }

    private void setProfitForOtherMarkets() {

        for (CoinMarket otherMarket : allOtherCoins) {
            otherMarket.profit = profitPercentage(otherMarket);
        }
    }


    @Override
    public String toString() {

        String result = "----------------------------------------------------------------------------------";

        result = lowestBuyCoin.getCoinType() + " " + coin + " - Profit " + percentageFormatter(profitPercentage(highestSellCoin)) + " Buy for " + priceFormatter(lowestBuyCoin.getOurBuyPrice()) + " at " +
                lowestBuyCoin.getMarket() + "   Sell for " + priceFormatter(highestSellCoin.getOurSellPrice()) + " at " + highestSellCoin.getMarket() + "\n";

        if (allOtherCoins.size() > 0) {

            result += "Other Markets" + "\n";
        }

        for (int i = 0; i < allOtherCoins.size(); i++) {
            CoinMarket coinMarket = allOtherCoins.get(i);
            result += " Sell at " + coinMarket.getMarket() + " for " + priceFormatter(coinMarket.getOurSellPrice()) + " " +
                    "Profit : " + profitPercentage(coinMarket) + "\n";

        }

        result += "----------------------------------------------------------------------------------------";
        return result;
    }


    public String toJSON() {
        return new Gson().toJson(this, ResultOfCalculation.class);
    }

    public String getCoin() {
        return coin;
    }

    public String getUnderScoreBTCCoin() {

        return coin.toLowerCase() + "_btc";
    }

    private String percentageFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        return df.format(value);
    }


    private String priceFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(value);
    }

    public ResultOfCalculation clone() {

        return new Gson().fromJson(toJSON(), ResultOfCalculation.class);

    }


}
