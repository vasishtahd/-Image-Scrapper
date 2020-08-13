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

public class CryptopiaCoinsStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {


        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.cryptopia.co.nz/coininfo/getcoininfo")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "d0310cb5-582a-09db-732e-427d986f9719")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("aaData");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONArray internalArray = jsonArray.getJSONArray(i);

                String name = "";
                String label = "";
                boolean isWalletActive = false;
                boolean isListingActive = false;

                for (int j = 0; j < internalArray.length(); j++) {
                    String key = internalArray.getString(j);
                    if (j == 1) {
                        name = key;
                    }
                    if (j == 2) {
                        label = key;
                    }
                    if (j == 8) {
                        if (key.equalsIgnoreCase("OK")) {
                            isWalletActive = true;
                        }
                    }
                    if (j == 10) {
                        if (key.equalsIgnoreCase("Active")) {
                            isListingActive = true;
                        }
                    }

                }
                CoinStatus coinInfo = toCoinStatus(name, label, isWalletActive, isListingActive);
                coinInfoList.add(coinInfo);
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

        CoinStatus coinInfo = new CoinStatus(Market.CRYPTOPIA, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
