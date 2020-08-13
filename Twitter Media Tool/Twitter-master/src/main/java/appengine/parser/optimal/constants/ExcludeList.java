package appengine.parser.optimal.constants;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.Market;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;
import org.jooq.Record5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.COINSTATUS;
import static appengine.parser.utils.OtherUtils.byteToBool;

public class ExcludeList {

    static HashMap<String, List<Market>> exclusions = new HashMap<>();

    static List<CoinStatus> coinStatusList = new ArrayList<>();

    public static boolean isExcluded(String coin, CoinMarket firstMarket, CoinMarket secondMarket) {
        if (exclusions.size() != 14) {
            exclusions.put("PXC", Arrays.asList(Market.CRYPTOPIA, Market.BITZ));
            exclusions.put("SMT", Arrays.asList(Market.SOUTHXCHANGE));
            exclusions.put("OC", Arrays.asList(Market.BITZ, Market.COINEXCHANGE));
            exclusions.put("BTM", Arrays.asList(Market.POLONEIX));
            exclusions.put("SBD", Arrays.asList(Market.HitBTC));
            exclusions.put("WRC", Arrays.asList(Market.CRYPTOPIA, Market.COINEXCHANGE));
            exclusions.put("POSW", Arrays.asList(Market.CRYPTOPIA));
            exclusions.put("DFS", Arrays.asList(Market.CRYPTOPIA));
            exclusions.put("BTA", Arrays.asList(Market.SOUTHXCHANGE));
            exclusions.put("GOLOS", Arrays.asList(Market.LIQUI));
            exclusions.put("INCNT", Arrays.asList(Market.LIQUI));
            exclusions.put("BCD", Arrays.asList(Market.BINANCE));
            exclusions.put("SLR", Arrays.asList(Market.COINEXCHANGE));//may 11th 2018
            exclusions.put("BCN", Arrays.asList(Market.HitBTC));//may 11th 2018
        }

        List<Market> markets = exclusions.get(coin.toUpperCase());
        if (markets != null && markets.size() > 0) {
            if (markets.size() == 2 && markets.contains(firstMarket.getMarket()) && (secondMarket != null && markets.contains(secondMarket.getMarket()))) {
                return true;
            }

            if (markets.size() == 1 && (markets.contains(firstMarket.getMarket()) || (secondMarket != null && markets.contains(secondMarket.getMarket())))) {
                return true;
            }
        }

        return checkExclusionFromDB(coin, firstMarket, secondMarket);

    }

    private static boolean checkExclusionFromDB(String coin, CoinMarket firstMarket, CoinMarket secondMarket) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record5<String, String, String, Byte, Byte> firstCoinRecord = dslContext.select(COINSTATUS.NAME, COINSTATUS.LABEL, COINSTATUS.MARKET,
                COINSTATUS.ISWALLETACTIVE, COINSTATUS.ISLISTINGACTIVE).from(COINSTATUS).where(COINSTATUS.LABEL.eq(coin).and(
                COINSTATUS.MARKET.eq(firstMarket.getMarket().name()))).fetchOne();

        CoinStatus firstCoinStatus = new CoinStatus(Market.valueOf(firstCoinRecord.value3()),
                firstCoinRecord.value1(), firstCoinRecord.value2(), byteToBool(firstCoinRecord.value4()),
                byteToBool(firstCoinRecord.value5()));


        if (!firstCoinStatus.isWalletActive()) {
            return true;
        }

        if (!firstCoinStatus.isListingActive()) {
            return true;
        }

        if (secondMarket != null) {

            Record5<String, String, String, Byte, Byte> secondCoinRecord = dslContext.select(COINSTATUS.NAME, COINSTATUS.LABEL, COINSTATUS.MARKET,
                    COINSTATUS.ISWALLETACTIVE, COINSTATUS.ISLISTINGACTIVE).from(COINSTATUS).where(COINSTATUS.LABEL.eq(coin).and(
                    COINSTATUS.MARKET.eq(secondMarket.getMarket().name()))).fetchOne();


            CoinStatus secondCoinStatus = new CoinStatus(Market.valueOf(secondCoinRecord.value3()),
                    secondCoinRecord.value1(), secondCoinRecord.value2(), byteToBool(secondCoinRecord.value4()),
                    byteToBool(secondCoinRecord.value5()));

            if (!secondCoinStatus.isWalletActive()) {
                return true;
            }
            if (!secondCoinStatus.isListingActive()) {
                return true;
            }

            if (!firstCoinStatus.getCoinName().equalsIgnoreCase("") &&
                    !secondCoinStatus.getCoinName().equalsIgnoreCase("")) {

                if (!isCoinNameEqual(firstCoinStatus.getCoinName(), secondCoinStatus.getCoinName())) {
                    return true;
                }
            }
        }

        return false;

    }

    public static boolean isCoinNameEqual(String firstCoinName, String secondCoinName) {
        firstCoinName = convertToGenericName(firstCoinName);
        secondCoinName = convertToGenericName(secondCoinName);

        if (firstCoinName.equalsIgnoreCase(secondCoinName)) {
            return true;
        }

        return false;
    }

    public static String convertToGenericName(String coinName) {
        coinName = coinName.trim();
        coinName = coinName.replace(" ", "");
        coinName = coinName.toLowerCase();
        coinName = coinName.replace("token", "");
        coinName = coinName.replace("coin", "");
        return coinName;

    }


}
