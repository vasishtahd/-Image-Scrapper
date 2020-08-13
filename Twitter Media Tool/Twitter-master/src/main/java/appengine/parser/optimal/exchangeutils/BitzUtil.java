package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.livecoinokex.utils.Ask;
import appengine.parser.optimal.livecoinokex.utils.Bid;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class BitzUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://www.bit-z.com/api_v1/tickerall")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "6f5d6414-0ce9-c6f1-2afc-40e477403d1e")
                    .build();
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject dataJSONObject = jsonObject.getJSONObject("data");

            Set<String> keys = dataJSONObject.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.contains("_btc")) {
                    JSONObject coinJSONObject = dataJSONObject.getJSONObject(key);
                    CoinMarket coinMarket = toCoinMarket(coinJSONObject, key);
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
        JSONObject coinJSONObject = (JSONObject) rawCoinMarket[0];
        String label = (String) rawCoinMarket[1];

        //for how much the holder is willing to sell
        CoinMarket coinMarket = new CoinMarket(Market.BITZ, label, coinJSONObject.getString("sell"),
                coinJSONObject.getString("buy"), coinJSONObject.getString("vol"));

        return coinMarket;
    }

    public TradeDepth getTradeDepth(String coin) {

        String label = coin.toLowerCase() + "_btc";

        TradeDepth tradeDepth = new TradeDepth();
        tradeDepth.coin = coin;

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.bit-z.com/api_v1/depth?coin="+label)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "4a6c3030-c5eb-0a2c-4640-1e1d95707958")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject datajsonObject = jsonObject.getJSONObject("data");
            JSONArray asksJsonArray = datajsonObject.getJSONArray("asks");
            JSONArray bidsJsonArray = datajsonObject.getJSONArray("bids");

            for (int i = 0; i < asksJsonArray.length(); i++) {

                int reverseIndex = asksJsonArray.length()-i-1;

                JSONArray askArray = asksJsonArray.getJSONArray(reverseIndex);
                String price = askArray.getString(0);
                String amount = askArray.getString(1);

                Ask ask = new Ask(Double.parseDouble(price), Double.parseDouble(amount));
                tradeDepth.askList.add(ask);
            }

            for (int i = 0; i < bidsJsonArray.length(); i++) {

                JSONArray bidArray = bidsJsonArray.getJSONArray(i);
                String price = bidArray.getString(0);
                String amount = bidArray.getString(1);

                Bid bid = new Bid(Double.parseDouble(price), Double.parseDouble(amount));
                tradeDepth.bidList.add(bid);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return tradeDepth;
    }
}
