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

public class BittrexCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://bittrex.com/api/v1.1/public/getcurrencies")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8917d71b-be27-6b40-99a1-32476181719c")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);


            JSONArray dataJsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < dataJsonArray.length(); i++) {

                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                String name = dataJsonObject.getString("CurrencyLong");
                String label = dataJsonObject.getString("Currency");
                boolean isWalletActive = false;
                boolean isListingActive = false;

                if (dataJsonObject.getBoolean("IsActive")) {
                    isWalletActive = true;
                    isListingActive = true;
                }

                CoinStatus coinStatus = toCoinStatus(name, label, isWalletActive, isListingActive);
                coinInfoList.add(coinStatus);

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

        CoinStatus coinInfo = new CoinStatus(Market.BITTREX, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
