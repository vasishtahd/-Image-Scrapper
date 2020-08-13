package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.liqui.LiquiExchange;
import org.knowm.xchange.liqui.dto.marketdata.LiquiTicker;
import org.knowm.xchange.liqui.service.LiquiMarketDataServiceRaw;

import java.util.*;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class LiquiUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        final Exchange liquiExchange = createTestExchange();
        final LiquiMarketDataServiceRaw liquiMarketDataServiceRaw = (LiquiMarketDataServiceRaw) liquiExchange.getMarketDataService();
        Map<String, LiquiTicker> liquiTickerMap = liquiMarketDataServiceRaw.getAllTickers();
        Set<String> keys = liquiTickerMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains("_btc")) {
                LiquiTicker liquiTicker = liquiTickerMap.get(key);
                CoinMarket coinMarket = toCoinMarket(liquiTicker, key);
                if (coinMarket.getOurBuyPrice() > 0) {
                    coinMarketList.add(coinMarket);
                }
            }
        }
    }

    public static Exchange createTestExchange() {
        final Exchange liquiExchange = ExchangeFactory.INSTANCE.createExchange(LiquiExchange.class.getName());
        liquiExchange.getExchangeSpecification().setApiKey("FIXUGEQP-XBFWFV0K-2ZP478V3-NSZ9YESA-C2INRYOY");
        liquiExchange.getExchangeSpecification().setSecretKey("01d28dc810f72f95ef1797b3900600717432e469d1f1cafcf3ca9c65cf2f402b");
        liquiExchange.applySpecification(liquiExchange.getExchangeSpecification());
        return liquiExchange;
    }

    @Override
    public List<CoinMarket> getCoinList() {
        if (coinMarketList == null || coinMarketList.size() == 0) {
            fetch();
        }
        return coinMarketList;
    }

    @Override
    public CoinMarket toCoinMarket(Object... rawCoinMarket) {
        LiquiTicker liquiTicker = (LiquiTicker) rawCoinMarket[0];
        String label = (String) rawCoinMarket[1];

        CoinMarket coinMarket = new CoinMarket(Market.LIQUI, label, liquiTicker.getSell(), liquiTicker.getBuy(), liquiTicker.getVol());

        return coinMarket;
    }
}
