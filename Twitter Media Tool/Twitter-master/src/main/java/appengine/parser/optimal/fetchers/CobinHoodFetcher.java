package appengine.parser.optimal.fetchers;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CobinHoodFetcher implements MarketUtil{

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {
        OkHttpClient client = new OkHttpClient();

        try{
            Request request = new Request.Builder()
                    .url("http://api.cobinhood.com/v1/market/tickers/")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "713500d8-3850-a834-457b-fefd5123c308")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject resultsObject = jsonObject.getJSONObject("result");
            JSONArray tickersArray = resultsObject.getJSONArray("tickers");

            for(int i=0;i<tickersArray.length();i++){
                JSONObject tickerObject  = tickersArray.getJSONObject(i);

                String tradingPair  =  tickerObject.getString("trading_pair_id");

                if(tradingPair.contains("-BTC")){

                    String coinname = tradingPair.split("-BTC")[0];
                    String sellprice = tickerObject.getString("lowest_ask");
                    String buyprice = tickerObject.getString("highest_bid");
                    String volume = tickerObject.getString("24h_volume");

                    CoinMarket coinMarket = toCoinMarket(coinname, sellprice, buyprice, volume);
                    coinMarketList.add(coinMarket);

                }
            }

        }
        catch (Exception e){
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
        String label = (String) rawCoinMarket[0];
        String askPrice = (String) rawCoinMarket[1];
        String bidPrice = (String) rawCoinMarket[2];
        String volume = (String) rawCoinMarket[3];

        CoinMarket coinMarket = new CoinMarket(Market.COBINHOOD, label, askPrice, bidPrice,
                volume);

        return coinMarket;
    }
}
