package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataService;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.util.*;

/**
 * Created by anand.kurapati on 05/01/18.
 */
public class PoloneixUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {
        try {
            Exchange poloniex = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
            MarketDataService marketDataService = poloniex.getMarketDataService();
            Map<String, PoloniexMarketData> marketDataList = ((PoloniexMarketDataService) marketDataService).getAllPoloniexTickers();

            Set<String> keys = marketDataList.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.contains("BTC_")) {
                    PoloniexMarketData poloniexMarketData = marketDataList.get(key);
                    CoinMarket coinMarket = toCoinMarket(poloniexMarketData, key);
                    coinMarketList.add(coinMarket);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        PoloniexMarketData poloniexMarketData = (PoloniexMarketData) rawCoinMarket[0];
        String label = (String) rawCoinMarket[1];
        String[] pair = label.split("BTC_");
        label = pair[1];

        CoinMarket coinMarket = new CoinMarket(Market.POLONEIX, label, poloniexMarketData.getLowestAsk(), poloniexMarketData.getHighestBid(),
                poloniexMarketData.getBaseVolume());
        return coinMarket;
    }


}
