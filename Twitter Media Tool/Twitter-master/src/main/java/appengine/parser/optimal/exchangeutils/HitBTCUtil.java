package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.hitbtc.v2.dto.HitbtcTicker;
import org.knowm.xchange.hitbtc.v2.service.HitbtcMarketDataServiceRaw;

import java.util.*;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class HitBTCUtil implements MarketUtil {

    private List<HitbtcTicker> cryptopiaTickerList;

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        try {

            Exchange hitbtcExchange = createExchange();
            hitbtcExchange.remoteInit();

            HitbtcMarketDataServiceRaw marketDataService = (HitbtcMarketDataServiceRaw) hitbtcExchange.getMarketDataService();
            Map<String, HitbtcTicker> tickers = marketDataService.getHitbtcTickers();

            Set<String> keys = tickers.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    HitbtcTicker hitbtcTicker = tickers.get(key);
                    if (key.substring(key.length() - 3, key.length()).equalsIgnoreCase("BTC") &&
                            !key.equalsIgnoreCase("BTCUSD")) {
                        key = key.substring(0, key.length() - 3);
                        CoinMarket coinMarket = toCoinMarket(hitbtcTicker, key);
                        coinMarketList.add(coinMarket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

        HitbtcTicker hitbtcTicker = (HitbtcTicker) rawCoinMarket[0];
        String label = (String) rawCoinMarket[1];

        CoinMarket coinMarket = new CoinMarket(Market.HitBTC, label, hitbtcTicker.getAsk(), hitbtcTicker.getBid(),
                hitbtcTicker.getVolume());

        return coinMarket;
    }

    public static Exchange createExchange() {

        ExchangeSpecification exSpec = new ExchangeSpecification(HitbtcExchange.class);
        exSpec.setApiKey("546c7e78190dcf14772d349591ccf5e3");
        exSpec.setSecretKey("95233d984740ece99a3c6361fd676954");

        return ExchangeFactory.INSTANCE.createExchange(exSpec);
    }

}
