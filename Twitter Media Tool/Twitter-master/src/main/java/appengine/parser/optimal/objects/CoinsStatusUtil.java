package appengine.parser.optimal.objects;

import java.util.List;

public interface CoinsStatusUtil {

    void fetch();

    List<CoinStatus> getCoinsStatusList();

    CoinStatus toCoinStatus(Object... rawCoinInfo);

}
