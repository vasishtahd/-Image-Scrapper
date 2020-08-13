package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.bitcurex.BitcurexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class BinanceUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();
    private List<BinanceTicker24h> tickers;

    @Override
    public void fetch() {

        try {

            URL url = new URL("https://api.binance.com/api/v1/ticker/24hr");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer json = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();


                /*OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://api.binance.com/api/v1/ticker/24hr")
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "489fa3d1-a999-6b98-d60d-3cd6533ab7e3")
                        .build();*/

               // Response response = client.newCall(request).execute();
                //String jsonString = response.body().string();

                String jsonString = json.toString();

                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String symbol = jsonObject.getString("symbol");
                    if (symbol.substring(symbol.length() - 3, symbol.length()).equalsIgnoreCase("BTC")) {
                        String label = symbol.substring(0, symbol.length() - 3);
                        String askPrice = jsonObject.getString("askPrice");
                        String bidPrice = jsonObject.getString("bidPrice");
                        String volume = jsonObject.getString("volume");

                        CoinMarket coinMarket = toCoinMarket(label, askPrice, bidPrice, volume);
                        coinMarketList.add(coinMarket);

                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getOrderBook(){

        Exchange binanceExchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());

        MarketDataService marketDataService = binanceExchange.getMarketDataService();
        try {
            generic(marketDataService, new CurrencyPair( "ltc","btc"));
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private static void generic(MarketDataService marketDataService, CurrencyPair pair) throws IOException {
        OrderBook orderBook = marketDataService.getOrderBook(pair, new Object[0]);
    }


    @Override
    public List<CoinMarket> getCoinList() {
        if(coinMarketList==null || coinMarketList.size()==0){
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

        CoinMarket coinMarket = new CoinMarket(Market.BINANCE, label, askPrice, bidPrice,
                volume);

        return coinMarket;

    }
}
