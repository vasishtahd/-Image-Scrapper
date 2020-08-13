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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class CoinExchangeUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    Map<String, String> codesLabelMap = new HashMap<>();
    Map<String, String> codesLabelInverseMap = new HashMap<>();

    @Override
    public void fetch() {

        getMarketsIds();


        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinexchange.io/api/v1/getmarketsummaries")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "377f33d5-503e-37f9-c229-b3694224b279")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < resultArray.length(); i++) {

                try {

                    JSONObject resultObject = resultArray.getJSONObject(i);

                    String marketId = resultObject.getString("MarketID");
                    String coin = codesLabelMap.get(marketId);
                    String volume = resultObject.getString("Volume");
                    String ourSellPrice = resultObject.getString("BidPrice");
                    String ourBuyPrice = resultObject.getString("AskPrice");
                    CoinMarket coinMarket = toCoinMarket(coin, ourSellPrice, ourBuyPrice, volume);
                    coinMarketList.add(coinMarket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {

        }


    }

    private void getMarketsIds() {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinexchange.io/api/v1/getmarkets")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8eb84f4d-7ddb-4ce5-cc78-4022ee2eb303")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject resultObject = resultArray.getJSONObject(i);
                if (resultObject.getString("BaseCurrencyCode").equalsIgnoreCase("BTC")) {

                    String marketId = resultObject.getString("MarketID");
                    String marketAssetCode = resultObject.getString("MarketAssetCode");
                    codesLabelMap.put(marketId, marketAssetCode);
                    codesLabelInverseMap.put(marketAssetCode, marketId);
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
        String coin = (String) rawCoinMarket[0];
        String ourSellPrice = (String) rawCoinMarket[1];
        String ourBuyPrice = (String) rawCoinMarket[2];
        String volume = (String) rawCoinMarket[3];
        CoinMarket coinMarket = new CoinMarket(Market.COINEXCHANGE, coin, ourBuyPrice, ourSellPrice, volume);
        return coinMarket;
    }

    public TradeDepth getTradeDepth(String coin) {


        TradeDepth tradeDepth = new TradeDepth();
        tradeDepth.coin = coin;

        getMarketsIds();

        String marketId = codesLabelInverseMap.get(coin.toUpperCase());

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinexchange.io/api/v1/getorderbook?market_id=" + marketId)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "3e12a991-5850-246a-d94e-73e12336b2ae")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject resultJsonObject = jsonObject.getJSONObject("result");

            JSONArray sellOrdersJsonArray = resultJsonObject.getJSONArray("SellOrders");
            JSONArray buyOrdersJsonArray = resultJsonObject.getJSONArray("BuyOrders");

            for (int i = 0; i < sellOrdersJsonArray.length(); i++) {

                JSONObject sellOrderObject = sellOrdersJsonArray.getJSONObject(i);

                String price = sellOrderObject.getString("Price");
                String quantity = sellOrderObject.getString("Quantity");

                Ask ask = new Ask(Double.parseDouble(price), Double.parseDouble(quantity));

                tradeDepth.askList.add(ask);
            }

            for (int i = 0; i < buyOrdersJsonArray.length(); i++) {
                JSONObject buyOrderObject = buyOrdersJsonArray.getJSONObject(i);

                String price = buyOrderObject.getString("Price");
                String quantity = buyOrderObject.getString("Quantity");

                Bid bid = new Bid(Double.parseDouble(price), Double.parseDouble(quantity));
                tradeDepth.bidList.add(bid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tradeDepth;

    }
}
