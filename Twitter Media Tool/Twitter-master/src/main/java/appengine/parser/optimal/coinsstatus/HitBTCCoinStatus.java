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

public class HitBTCCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.hitbtc.com/api/2/public/currency")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONArray dataJsonArray = new JSONArray(jsonString);

            for (int i = 0; i < dataJsonArray.length(); i++) {

                try {

                    JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                    String name = dataJsonObject.getString("fullName");
                    String label = dataJsonObject.getString("id");
                    boolean isWalletActive = false;
                    boolean isListingActive = false;

                    isWalletActive = dataJsonObject.getBoolean("transferEnabled");
                    isListingActive = !dataJsonObject.getBoolean("delisted");

                    CoinStatus coinStatus = toCoinStatus(name, label, isWalletActive, isListingActive);
                    coinInfoList.add(coinStatus);
                }
                catch (Exception e){
                    e.printStackTrace();
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

        CoinStatus coinInfo = new CoinStatus(Market.HitBTC, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
