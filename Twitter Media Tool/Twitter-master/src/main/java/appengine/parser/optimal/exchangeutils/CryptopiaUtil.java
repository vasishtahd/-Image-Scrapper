package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 05/01/18.
 */
public class CryptopiaUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();


    @Override
    public void fetch() {
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.cryptopia.co.nz/api/GetMarkets")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "2767028b-b145-a2b6-e518-327a269707d5")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject coinObject = jsonArray.getJSONObject(i);
                String label = coinObject.getString("Label");
                if (label.contains("/BTC")) {
                    Double ourbuyprice = coinObject.getDouble("AskPrice");
                    Double oursellprice = coinObject.getDouble("BidPrice");
                    Double buybasevolume = coinObject.getDouble("BuyBaseVolume");
                    CoinMarket coinMarket = toCoinMarket(label, ourbuyprice, oursellprice, buybasevolume);
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
        String label = (String) rawCoinMarket[0];
        Double buyprice = (Double) rawCoinMarket[1];
        Double sellprice = (Double) rawCoinMarket[2];
        Double volume = (Double) rawCoinMarket[3];

        CoinMarket coinMarket = new CoinMarket(Market.CRYPTOPIA, label, doubleFormatter(buyprice),
                doubleFormatter(sellprice), doubleFormatter(volume));
        return coinMarket;
    }

    private String doubleFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(value);
    }
}
