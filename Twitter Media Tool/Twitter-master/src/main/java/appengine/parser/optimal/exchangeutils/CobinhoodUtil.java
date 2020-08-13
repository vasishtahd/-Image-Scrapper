package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.livecoinokex.utils.Ask;
import appengine.parser.optimal.livecoinokex.utils.Bid;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.jooq.Record6;
import org.jooq.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.tables.Fetcher.FETCHER;

/**
 * Created by anand.kurapati on 09/01/18.
 */
public class CobinhoodUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();


    @Override
    public void fetch() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record6<String, String, Double, Double, Double, Timestamp>> result =
                dslContext.select(FETCHER.COIN, FETCHER.MARKET, FETCHER.BUY_FOR,
                        FETCHER.SELL_FOR, FETCHER.VOLUME, FETCHER.TIME).from(FETCHER)
                        .where(FETCHER.MARKET.eq(Market.COBINHOOD.name())).fetch();


        for (Record6<String, String, Double, Double, Double, Timestamp> record6 : result) {
            CoinMarket coinMarket = toCoinMarket(record6.value1(), String.valueOf(record6.value3()),
                    String.valueOf(record6.value4()), String.valueOf(record6.value5()));
            coinMarketList.add(coinMarket);
        }
    }

    public void fetchCoinHills() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinhills.com/api/internal/market_read.php?pri_code=&sec_code=&src_code=cobinhood&order=sec_type-" +
                            "desc%2Csec_code-asc%2Cvolume_btc-desc&page=0&row=250&_=" + System.currentTimeMillis())
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "40f5e258-5554-e542-f356-4a89a1c8c455")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray coinsJSONArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < coinsJSONArray.length(); i++) {

                try {

                    JSONObject coinObject = coinsJSONArray.getJSONObject(i);
                    if (!coinObject.getString("sec_code").equalsIgnoreCase("BTC")) {
                        continue;
                    }
                    CoinMarket coinMarket = toCoinMarket(coinObject.getString("pri_code"),
                            coinObject.getString("price"), coinObject.getString("volume"));
                    coinMarketList.add(coinMarket);
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

        String label = (String) rawCoinMarket[0];
        String askPrice = (String) rawCoinMarket[1];
        String bidPrice = (String) rawCoinMarket[2];
        String volume = (String) rawCoinMarket[3];

        CoinMarket coinMarket = new CoinMarket(Market.COBINHOOD, label, askPrice, bidPrice,
                volume);

        return coinMarket;

    }

    public TradeDepth getTradeDepth(String coin) {

        TradeDepth tradeDepth = new TradeDepth();
        tradeDepth.coin = coin;

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://api.cobinhood.com/v1/market/orderbooks/" + coin.toUpperCase() + "-BTC")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "f0cf41fc-8c84-f033-6da1-d9f7efa4ecda")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            JSONObject orderObject = resultObject.getJSONObject("orderbook");
            JSONArray bids = orderObject.getJSONArray("bids");

            for (int i = 0; i < bids.length(); i++) {

                JSONArray bidsJSONArray = bids.getJSONArray(i);
                String price = bidsJSONArray.getString(0);
                String amount = bidsJSONArray.getString(2);

                Bid bid = new Bid(Double.parseDouble(price), Double.parseDouble(amount));
                tradeDepth.bidList.add(bid);

            }

            JSONArray asks = orderObject.getJSONArray("asks");

            for (int i = 0; i < asks.length(); i++) {

                JSONArray asksJSONArray = asks.getJSONArray(i);
                String price = asksJSONArray.getString(0);
                String amount = asksJSONArray.getString(2);

                Ask ask = new Ask(Double.parseDouble(price), Double.parseDouble(amount));
                tradeDepth.askList.add(ask);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return tradeDepth;
    }


    public CoinMarket toCoinMarketCoinHills(Object... rawCoinMarket) {
        String coinname = (String) rawCoinMarket[0];
        String last = (String) rawCoinMarket[1];
        String volume = (String) rawCoinMarket[2];

        CoinMarket coinMarket = new CoinMarket(Market.COBINHOOD, coinname, last, last, volume);
        return coinMarket;
    }


}
