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
import org.knowm.xchange.dto.marketdata.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class SouthXchangeUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.southxchange.com/api/prices")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "9bb2bf66-92b6-91cf-ebb4-826b42848ec0")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String coinName = jsonObject.getString("Market");

                try {
                    if (coinName.contains("/BTC")) {
                        Double ourSellPrice = jsonObject.getDouble("Bid");
                        Double ourBuyPrice = jsonObject.getDouble("Ask");
                        Double volume = jsonObject.getDouble("Volume24Hr");

                        CoinMarket coinMarket = toCoinMarket(coinName, ourSellPrice, ourBuyPrice, volume);
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

        String coinName = (String) rawCoinMarket[0];
        Double ourSellPrice = (Double) rawCoinMarket[1];
        Double ourBuyPrice = (Double) rawCoinMarket[2];
        Double volume = (Double) rawCoinMarket[3];

        CoinMarket coinMarket = new CoinMarket(Market.SOUTHXCHANGE, coinName, String.valueOf(ourBuyPrice), String.valueOf(ourSellPrice),
                String.valueOf(volume));

        return coinMarket;
    }

    public TradeDepth getTradeDepth(String coin){

        TradeDepth tradeDepth = new TradeDepth();
        tradeDepth.coin = coin;


        try{

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.southxchange.com/api/book/"+coin.toUpperCase()+"/BTC")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "a2ccc185-560f-a008-687d-69e25b14c4d0")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString  = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray buyOrdersJSONArray = jsonObject.getJSONArray("BuyOrders");
            JSONArray sellOrdersJSONArray = jsonObject.getJSONArray("SellOrders");

            for(int i=0;i<buyOrdersJSONArray.length();i++){
                JSONObject buyOrderJsonObject = buyOrdersJSONArray.getJSONObject(i);

                Double price = buyOrderJsonObject.getDouble("Price");
                Double amount = buyOrderJsonObject.getDouble("Amount");

                Bid bid = new Bid(price,amount);
                tradeDepth.bidList.add(bid);
            }

            for(int i=0;i<sellOrdersJSONArray.length();i++){
                JSONObject sellOrderJsonObject = sellOrdersJSONArray.getJSONObject(i);

                Double price = sellOrderJsonObject.getDouble("Price");
                Double amount = sellOrderJsonObject.getDouble("Amount");

                Ask ask = new Ask(price,amount);
                tradeDepth.askList.add(ask);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }

        return tradeDepth;

    }
}
