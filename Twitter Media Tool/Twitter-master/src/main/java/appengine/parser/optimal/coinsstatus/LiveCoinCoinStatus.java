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

public class LiveCoinCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.livecoin.net/info/coinInfo")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "d8a3a28a-2d3d-f1ff-15df-9034fe28def8")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);


            JSONArray dataJsonArray = jsonObject.getJSONArray("info");

            for (int i = 0; i < dataJsonArray.length(); i++) {

                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                String name = dataJsonObject.getString("name");
                String label = dataJsonObject.getString("symbol");
                boolean isWalletActive = false;
                boolean isListingActive = false;

                if (dataJsonObject.getString("walletStatus").equalsIgnoreCase("normal")) {
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

        CoinStatus coinInfo = new CoinStatus(Market.LIVECOIN, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
