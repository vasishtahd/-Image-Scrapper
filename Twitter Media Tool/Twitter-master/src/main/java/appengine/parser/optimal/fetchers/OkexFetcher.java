package appengine.parser.optimal.fetchers;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OkexFetcher implements MarketUtil {

    private static String[] coins = {"LTC", "ETH", "ETC", "BCH", "XRP", "XEM", "XLM", "IOTA", "1ST", "AAC",
            "ACE", "ACT", "AIDOC", "AMM", "ARK", "AST", "ATL", "AVT", "BCD", "BCX", "BNT", "BRD", "BT1", "BT2", "BTG",
            "BTM", "CAG", "CAN", "CMT", "CTR", "CVC", "DASH", "DAT", "DGB", "DGD", "DNA", "DNT", "DPY", "EDO", "ELF", "ENG",
            "EOS", "ETF", "EVX", "F4SBTC", "FAIR", "FUN", "GAS", "GNT", "GNX", "HOT", "HSR", "ICN", "ICX", "INS", "INT", "IOST",
            "IPC", "ITC", "KCASH", "KEY", "KNC", "LA", "LEND", "LEV", "LIGHT", ""};

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        OkHttpClient client = new OkHttpClient();

        for (String label : coins) {

            try {

                String symbol_to_be_requested = label.toLowerCase() + "_btc";

                Request request = new Request.Builder()
                        .url("https://www.okex.com/api/v1/ticker.do?symbol=" + symbol_to_be_requested)
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "a4b4bc52-3668-9439-53ef-3f4eb0269f9c")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();


                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject tickerObject = jsonObject.getJSONObject("ticker");


                String sellprice = tickerObject.getString("sell");
                String buyprice = tickerObject.getString("buy");
                String volume = tickerObject.getString("vol");

                CoinMarket coinMarket = toCoinMarket(label, sellprice, buyprice, volume);
                coinMarketList.add(coinMarket);

            } catch (Exception e) {
                e.printStackTrace();
            }

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
        String label = (String) rawCoinMarket[0];
        String askPrice = (String) rawCoinMarket[1];
        String bidPrice = (String) rawCoinMarket[2];
        String volume = (String) rawCoinMarket[3];

        CoinMarket coinMarket = new CoinMarket(Market.OKEX, label, askPrice, bidPrice,
                volume);

        return coinMarket;
    }
}
