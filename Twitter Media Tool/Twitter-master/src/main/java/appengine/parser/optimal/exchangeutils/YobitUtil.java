package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.yobit.YoBitExchange;
import org.knowm.xchange.yobit.service.YoBitMarketDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class YobitUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    YoBitMarketDataService yoBitMarketDataService;

    @Override
    public void fetch() {
        if (yoBitMarketDataService == null) {
            initYoBit();
        }
    }


    private void initYoBit() {
        Exchange yoBitExchange = ExchangeFactory.INSTANCE.createExchange(YoBitExchange.class.getName(), "", "",
                "7ECABF40BE54588B162B0055FF4F7444",
                "28be9a9e22acc24fcb944e349b0adb11");
        yoBitMarketDataService = (YoBitMarketDataService) yoBitExchange.getMarketDataService();
    }

    @Override
    public List<CoinMarket> getCoinList() {
        return null;
    }

    @Override
    public CoinMarket toCoinMarket(Object... rawCoinMarket) {
        return null;
    }
}
