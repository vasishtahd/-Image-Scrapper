package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalupdateOperation;
import appengine.parser.optimal.exchangeutils.BinanceUtil;
import appengine.parser.optimal.fetchers.CobinHoodFetcher;
import appengine.parser.optimal.fetchers.OkexFetcher;
import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.MarketUtil;
import appengine.parser.utils.DataBaseConnector;
import appengine.parser.utils.TimeUtils;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.FETCHER;
import static appengine.parser.mysqlmodels.Tables.FETCHERHISTORY;
import static appengine.parser.mysqlmodels.Tables.OPTIMALUPDATE;

public class Fetcher {


    public String fetchOkex() {

        OkexFetcher okexFetcher = new OkexFetcher();
        List<CoinMarket> coinMarketList = okexFetcher.getCoinList();
        updateTickerInDB(coinMarketList);


        return returnArrayResults(coinMarketList);
    }

    public String fetchCobinHood() {

        CobinHoodFetcher cobinHoodFetcher = new CobinHoodFetcher();
        List<CoinMarket> coinMarketList = cobinHoodFetcher.getCoinList();
        updateTickerInDB(coinMarketList);

        return returnArrayResults(coinMarketList);
    }

    public String fetchHistoryOkex() {

        OkexFetcher okexFetcher = new OkexFetcher();
        List<CoinMarket> coinMarketList = okexFetcher.getCoinList();
        insertTickerInDB(coinMarketList);

        return returnArrayResults(coinMarketList);
    }

    public String fetchHistoryBinance() {

        System.setProperty("java.net.preferIPv4Stack" , "true");
        System.setProperty("java.net.preferIPv6Addresses" , "false");

        MarketUtil binanceUtil = new BinanceUtil();
        List<CoinMarket> coinMarketList =  binanceUtil.getCoinList();
        insertTickerInDB(coinMarketList);

        return returnArrayResults(coinMarketList);
    }

    public String returnArrayResults(List<CoinMarket> coinMarketList) {

        String printString = "\n";

        for (int i = 0; i < coinMarketList.size(); i++) {
            CoinMarket coinMarket = coinMarketList.get(i);
            printString += coinMarket.toString() + "\n";
        }

        return printString;
    }

    private void updateTickerInDB(List<CoinMarket> coinMarketList) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        for (CoinMarket coinMarket : coinMarketList) {
            try {
                dslContext.insertInto(FETCHER, FETCHER.COIN,
                        FETCHER.MARKET, FETCHER.BUY_FOR, FETCHER.SELL_FOR,
                        FETCHER.VOLUME)
                        .values(coinMarket.getCoinName(), coinMarket.getMarket().name(),
                                coinMarket.getOurBuyPrice(), coinMarket.getOurSellPrice(),
                                coinMarket.getTotalVolume()).onDuplicateKeyUpdate()
                        .set(FETCHER.BUY_FOR, coinMarket.getOurBuyPrice())
                        .set(FETCHER.SELL_FOR, coinMarket.getOurSellPrice())
                        .set(FETCHER.VOLUME, coinMarket.getTotalVolume())
                        .set(FETCHER.TIME, TimeUtils.getCurrentTime())
                        .execute();
            }catch (Exception e ){
                e.printStackTrace();
            }
        }

    }

    private void insertTickerInDB(List<CoinMarket> coinMarketList) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        for (CoinMarket coinMarket : coinMarketList) {
            try {
                dslContext.insertInto(FETCHERHISTORY, FETCHERHISTORY.COIN,
                        FETCHERHISTORY.MARKET, FETCHERHISTORY.BUY_FOR, FETCHERHISTORY.SELL_FOR,
                        FETCHERHISTORY.VOLUME)
                        .values(coinMarket.getCoinName(), coinMarket.getMarket().name(),
                                coinMarket.getOurBuyPrice(), coinMarket.getOurSellPrice(),
                                coinMarket.getTotalVolume()).onDuplicateKeyUpdate()
                        .set(FETCHERHISTORY.BUY_FOR, coinMarket.getOurBuyPrice())
                        .set(FETCHERHISTORY.SELL_FOR, coinMarket.getOurSellPrice())
                        .set(FETCHERHISTORY.VOLUME, coinMarket.getTotalVolume())
                        .set(FETCHERHISTORY.TIME, TimeUtils.getCurrentTime())
                        .execute();
            }catch (Exception e ){
                e.printStackTrace();
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

}
