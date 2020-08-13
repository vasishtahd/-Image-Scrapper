package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalupdateOperation;
import appengine.parser.optimal.constants.MarketConstants;
import appengine.parser.optimal.exchangeutils.*;
import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static appengine.parser.mysqlmodels.Tables.OPTIMALJSON;
import static appengine.parser.mysqlmodels.Tables.OPTIMALUPDATE;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class CoinCalculator {

    String printString = "\n";

    double MINIMUM_PROFIT_PERCENTAGE = 2;

    String buyFromMarket = null;


    Map<String, List<CoinMarket>> coinsMarketMap = new HashMap<>();

    List<List<CoinMarket>> allCoinsList = new ArrayList<>();

    List<CoinMarket> binanceList = new ArrayList<>();
    List<CoinMarket> bitzList = new ArrayList<>();
    List<CoinMarket> cryptopiaList = new ArrayList<>();
    List<CoinMarket> hitBTCList = new ArrayList<>();
    List<CoinMarket> liquiList = new ArrayList<>();
    List<CoinMarket> livecoinList = new ArrayList<>();
    List<CoinMarket> poloneixList = new ArrayList<>();
    List<CoinMarket> yobitList = new ArrayList<>();
    List<CoinMarket> okexList = new ArrayList<>();
    List<CoinMarket> cobinHoodList = new ArrayList<>();
    List<CoinMarket> coinExchangeList = new ArrayList<>();
    List<CoinMarket> southXchangeList = new ArrayList<>();
    List<CoinMarket> bittrexList = new ArrayList<>();

    public String fetchAll() {

        fetchBinance();
        fetchBitZ();
        fetchCryptopia();
        fetchHitBTC();
        fetchLiqui();
        fetchLivecoin();
        fetchPoloneix();
        fetchCobinHood();
        fetchCoinExchange();
        fetchSouthXChange();
        fetchOkex();
        fetchBittrex();
        //fetchYobit();

        createCoinsMap();

        deleteOldFromDB();

        calculateHighesAndLowest();


        return printString;
    }

    public void setMinimumProfitPercentage(double profitPercentage) {

        MINIMUM_PROFIT_PERCENTAGE = profitPercentage;
    }

    public void setBuyFromMarket(String buyFromMarket) {
        this.buyFromMarket = buyFromMarket;
    }

    public String fetchAll(String[] includes) {

        ArrayList<String> includeList = new ArrayList<>();
        for (int i = 0; i < includes.length; i++) {
            includeList.add(includes[i]);
        }

        if (includeList.contains(MarketConstants.BinanceString)) {
            fetchBinance();
        }
        if (includeList.contains(MarketConstants.BitzString)) {
            fetchBitZ();
        }
        if (includeList.contains(MarketConstants.CrypropiaString)) {
            fetchCryptopia();
        }
        if (includeList.contains(MarketConstants.HitbtcString)) {
            fetchHitBTC();
        }
        if (includeList.contains(MarketConstants.LiquiString)) {
            fetchLiqui();
        }
        if (includeList.contains(MarketConstants.LivecoinString)) {
            fetchLivecoin();
        }
        if (includeList.contains(MarketConstants.PoloneixString)) {
            fetchPoloneix();
        }
        if (includeList.contains("yobit")) {
            fetchYobit();
        }
        if (includeList.contains(MarketConstants.OkexString)) {
            fetchOkex();
        }
        if (includeList.contains(MarketConstants.CobinhoodString)) {
            fetchCobinHood();
        }
        if (includeList.contains(MarketConstants.CoinExchangeString)) {
            fetchCoinExchange();
        }
        if (includeList.contains(MarketConstants.SouthXChangeString)) {
            fetchSouthXChange();
        }
        if (includeList.contains(MarketConstants.BittrexString)) {
            fetchBittrex();
        }

        createCoinsMap();

        deleteOldFromDB();

        calculateHighesAndLowest();


        return printString;
    }


    private void calculateHighesAndLowest() {

        Timestamp timestamp = getCurrentTime();
        updateCalculationTimeInDB(timestamp);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<String> keyset = coinsMarketMap.keySet();

        Iterator<String> iterator = keyset.iterator();

        while (iterator.hasNext()) {
            String coin = iterator.next();
            List<CoinMarket> allMarketsOfCoins = coinsMarketMap.get(coin);

            Double lowestBuy = new Double(100);
            Double highestSell = new Double(-1);
            CoinMarket lowestPurchaseCoin = null;
            CoinMarket highestSellCoin = null;

            for (int i = 0; i < allMarketsOfCoins.size(); i++) {
                CoinMarket coinMarket = allMarketsOfCoins.get(i);

                if (coinMarket.getOurSellPrice() > highestSell) {
                    highestSell = coinMarket.getOurSellPrice();
                    highestSellCoin = coinMarket;
                }

                if (coinMarket.getOurBuyPrice() < lowestBuy) {
                    if (buyFromMarket != null && !coinMarket.equalsMarket(buyFromMarket)) {
                        continue;
                    }
                    lowestBuy = coinMarket.getOurBuyPrice();
                    lowestPurchaseCoin = coinMarket;
                }

            }

            Double profitPercentage = ((highestSell - lowestBuy) / lowestBuy) * 100;


            if (lowestPurchaseCoin != null && highestSellCoin != null) {
                ResultOfCalculation resultOfCalculation =
                        new ResultOfCalculation(coin, lowestPurchaseCoin, highestSellCoin, allMarketsOfCoins);
                print(resultOfCalculation.toString());
                insertResultInDB(resultOfCalculation);
            }
        }


    }


    private void createCoinsMap() {

        coinsMarketMap = new HashMap<>();

        for (int i = 0; i < allCoinsList.size(); i++) {
            List<CoinMarket> marketList = allCoinsList.get(i);
            if (marketList != null) {
                for (int j = 0; j < marketList.size(); j++) {
                    CoinMarket coinMarket = marketList.get(j);
                    List<CoinMarket> coinMarketList;
                    if (coinsMarketMap.containsKey(coinMarket.getCoinName())) {
                        coinMarketList = coinsMarketMap.get(coinMarket.getCoinName());
                        coinMarketList.add(coinMarket);
                    } else {
                        coinMarketList = new ArrayList<CoinMarket>();
                        coinMarketList.add(coinMarket);
                        coinsMarketMap.put(coinMarket.getCoinName(), coinMarketList);
                    }
                }
            }

        }

    }

    private void updateCalculationTimeInDB(Timestamp timestamp) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALUPDATE, OPTIMALUPDATE.OPERATION)
                .values(OptimalupdateOperation.COINCALCULATOR).onDuplicateKeyUpdate()
                .set(OPTIMALUPDATE.UPDATEDTIME, timestamp)
                .execute();
    }

    private void deleteOldFromDB() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.deleteFrom(OPTIMALJSON).execute();
    }

    private void insertResultInDB(ResultOfCalculation resultOfCalculation) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALJSON, OPTIMALJSON.COINLABEL, OPTIMALJSON.JSON)
                .values(resultOfCalculation.getCoin(), resultOfCalculation.toJSON()).execute();
    }

    private Timestamp getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String result = sdf.format(calendar.getTime());
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp;
    }

    public String fetchSouthXChange() {
        SouthXchangeUtil marketUtil = new SouthXchangeUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        southXchangeList = coinMarketList;
        allCoinsList.add(southXchangeList);
        return returnResults(coinMarketList);
    }

    public String fetchCoinExchange() {
        CoinExchangeUtil marketUtil = new CoinExchangeUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        coinExchangeList = coinMarketList;
        allCoinsList.add(coinExchangeList);
        return returnResults(coinMarketList);
    }

    public String fetchCobinHood() {
        CobinhoodUtil marketUtil = new CobinhoodUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        cobinHoodList = coinMarketList;
        allCoinsList.add(cobinHoodList);
        return returnResults(coinMarketList);

    }


    public String fetchOkex() {
        OkexUtil marketUtil = new OkexUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        okexList = coinMarketList;
        allCoinsList.add(okexList);
        return returnResults(coinMarketList);
    }

    public String fetchBinance() {
        BinanceUtil marketUtil = new BinanceUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        binanceList = coinMarketList;
        allCoinsList.add(binanceList);
        return returnResults(coinMarketList);
    }

    public String fetchBitZ() {
        BitzUtil marketUtil = new BitzUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        bitzList = coinMarketList;
        allCoinsList.add(bitzList);
        return returnResults(coinMarketList);
    }

    public String fetchCryptopia() {
        CryptopiaUtil marketUtil = new CryptopiaUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        cryptopiaList = coinMarketList;
        allCoinsList.add(cryptopiaList);
        return returnResults(coinMarketList);
    }

    public String fetchHitBTC() {
        HitBTCUtil marketUtil = new HitBTCUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        hitBTCList = coinMarketList;
        allCoinsList.add(hitBTCList);
        return returnResults(coinMarketList);
    }

    public String fetchLiqui() {
        LiquiUtil marketUtil = new LiquiUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        liquiList = coinMarketList;
        allCoinsList.add(liquiList);
        return returnResults(coinMarketList);
    }

    public String fetchLivecoin() {
        LivecoinUtil marketUtil = new LivecoinUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        livecoinList = coinMarketList;
        allCoinsList.add(livecoinList);
        return returnResults(coinMarketList);
    }

    public String fetchPoloneix() {
        PoloneixUtil marketUtil = new PoloneixUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        poloneixList = coinMarketList;
        allCoinsList.add(poloneixList);
        return returnResults(coinMarketList);
    }

    public String fetchYobit() {
        YobitUtil marketUtil = new YobitUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        yobitList = coinMarketList;
        allCoinsList.add(yobitList);
        return returnResults(coinMarketList);
    }

    public String fetchBittrex() {
        BittrexUtil marketUtil = new BittrexUtil();
        List<CoinMarket> coinMarketList = marketUtil.getCoinList();
        bittrexList = coinMarketList;
        allCoinsList.add(bittrexList);
        return returnResults(coinMarketList);
    }

    public String printLiveCoin() {
        fetchLivecoin();
        return returnArrayResults(livecoinList);
    }

    public String returnArrayResults(List<CoinMarket> coinMarketList) {

        String printString = "\n";

        for (int i = 0; i < coinMarketList.size(); i++) {
            CoinMarket coinMarket = coinMarketList.get(i);
            printString += coinMarket.toString() + "\n";
        }

        return printString;
    }

    public String returnResults(List<CoinMarket> coinMarketList) {
       /* for (int i = 0; i < coinMarketList.size(); i++) {
            CoinMarket coinMarket = coinMarketList.get(i);
            print(coinMarket.toString());
        }*/

        return printString;
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

    private void print(String text) {
        printString += text + "\n";
    }
}
