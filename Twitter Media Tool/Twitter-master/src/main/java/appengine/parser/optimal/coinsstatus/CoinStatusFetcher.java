package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;

import java.util.List;

import static appengine.parser.mysqlmodels.Tables.COINSTATUS;
import static appengine.parser.utils.OtherUtils.boolToByte;
import static appengine.parser.utils.TimeUtils.getCurrentTime;

public class CoinStatusFetcher {

    public void fetch() {

        CoinsStatusUtil coinStatus = null;
        for (Market market : Market.values()) {
            try {

                if (market == Market.BINANCE) {
                    coinStatus = new BinanceCoinStatus();
                }
                if (market == Market.BITTREX) {
                    coinStatus = new BittrexCoinStatus();
                }
                if (market == Market.COINEXCHANGE) {
                    coinStatus = new CoinExchangeCoinStatus();
                }
                if (market == Market.CRYPTOPIA) {
                    coinStatus = new CryptopiaCoinsStatus();
                }
                if (market == Market.HitBTC) {
                    coinStatus = new HitBTCCoinStatus();
                }
                if (market == Market.LIVECOIN) {
                    coinStatus = new LiveCoinCoinStatus();
                }
                if (market == Market.OKEX) {
                    coinStatus = new OkexCoinStatus();
                }
                if (market == Market.POLONEIX) {
                    coinStatus = new PoloniexCoinStatus();
                }
                if (market == Market.SOUTHXCHANGE) {
                    coinStatus = new SouthXchangeCoinStatus();
                }
                if (market == Market.BITZ) {
                    coinStatus = new BitzCoinStatus();
                }
                if (market == Market.COBINHOOD) {
                    coinStatus = new CobinhoodCoinStatus();
                }
                if (market == Market.LIQUI) {
                    coinStatus = new LiquiCoinStatus();
                }

                updateOnDB(coinStatus.getCoinsStatusList());

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    private void updateOnDB(List<CoinStatus> coinStatusList) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        for (int i = 0; i < coinStatusList.size(); i++) {
            CoinStatus coinStatus = coinStatusList.get(i);
            dslContext.insertInto(COINSTATUS, COINSTATUS.NAME, COINSTATUS.LABEL, COINSTATUS.MARKET,
                    COINSTATUS.ISWALLETACTIVE, COINSTATUS.ISLISTINGACTIVE)
                    .values(coinStatus.getCoinName(), coinStatus.getLabel(), coinStatus.getMarket().name(),
                            boolToByte(coinStatus.isWalletActive()), boolToByte(coinStatus.isListingActive()))
                    .onDuplicateKeyUpdate()
                    .set(COINSTATUS.ISWALLETACTIVE, boolToByte(coinStatus.isWalletActive()))
                    .set(COINSTATUS.ISLISTINGACTIVE, boolToByte(coinStatus.isListingActive()))
                    .set(COINSTATUS.TIME, getCurrentTime())
                    //.set(COINSTATUS.NAME, coinStatus.getCoinName())
                    .execute();

        }
    }
}
