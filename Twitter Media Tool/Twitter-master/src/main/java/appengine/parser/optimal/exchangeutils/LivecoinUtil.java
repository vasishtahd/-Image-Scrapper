package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.livecoin.LivecoinExchange;
import org.knowm.xchange.livecoin.dto.marketdata.LivecoinTicker;
import org.knowm.xchange.livecoin.service.LivecoinMarketDataServiceRaw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class LivecoinUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            Exchange exchange = ExchangeFactory.INSTANCE.createExchange(LivecoinExchange.class.getName());
            LivecoinMarketDataServiceRaw livecoinMarketDataServiceRaw = (LivecoinMarketDataServiceRaw) exchange.getMarketDataService();
            List<LivecoinTicker> livecoinTickerList = livecoinMarketDataServiceRaw.getAllTickers();
            for (int i = 0; i < livecoinTickerList.size(); i++) {
                LivecoinTicker livecoinTicker = livecoinTickerList.get(i);
                if (livecoinTicker.getSymbol().contains("/BTC")) {
                    CoinMarket coinMarket = toCoinMarket(livecoinTicker);
                    coinMarketList.add(coinMarket);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CoinMarket> getCoinList() {
        if(coinMarketList==null || coinMarketList.size()==0){
            fetch();
        }
        return coinMarketList;
    }

    @Override
    public CoinMarket toCoinMarket(Object... rawObject) {

        LivecoinTicker livecoinTicker = (LivecoinTicker) rawObject[0];

        CoinMarket coinMarket = new CoinMarket(Market.LIVECOIN, livecoinTicker.getCur(), livecoinTicker.getBestAsk(),
                livecoinTicker.getBestBid(), livecoinTicker.getVolume());

        return coinMarket;
    }
}
