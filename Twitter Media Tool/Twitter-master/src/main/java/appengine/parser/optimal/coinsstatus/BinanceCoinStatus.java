package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BinanceCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.binance.com/exchange/public/product")
                    .get()
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.binance.com/")
                    .addHeader("clienttype", "web")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataJsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < dataJsonArray.length(); i++) {

                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                if (dataJsonObject.getString("symbol").contains("BTC")) {

                    String name = dataJsonObject.getString("baseAssetName");
                    String label = dataJsonObject.getString("baseAsset");
                    boolean isWalletActive = dataJsonObject.getBoolean("active");
                    boolean isListingActive = false;
                    if (dataJsonObject.getString("status").equalsIgnoreCase("TRADING")) {
                        isListingActive = true;
                    }

                    CoinStatus coinStatus = toCoinStatus(name, label, isWalletActive, isListingActive);
                    coinInfoList.add(coinStatus);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<CoinStatus> getCoinsStatusList() {
        if (coinInfoList == null || coinInfoList.size() == 0) {
            fetch();
        }
        return coinInfoList;
    }

    @Override
    public CoinStatus toCoinStatus(Object... rawCoinInfo) {
        String coinName = (String) rawCoinInfo[0];
        String label = (String) rawCoinInfo[1];
        boolean isWalletActive = (boolean) rawCoinInfo[2];
        boolean isListingActive = (boolean) rawCoinInfo[3];

        CoinStatus coinInfo = new CoinStatus(Market.BINANCE, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
