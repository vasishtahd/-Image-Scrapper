package appengine.parser.optimal.objects;

import java.util.List;

/**
 * Created by anand.kurapati on 05/01/18.
 */
public interface MarketUtil {

    void fetch();

    List<CoinMarket> getCoinList();

    CoinMarket toCoinMarket(Object... rawCoinMarket);
}
