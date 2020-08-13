package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SouthXchangeCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.southxchange.com/Home/GetWalletsInfo")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "cd179da6-ca61-3fad-295d-3ea1f3a22f0e")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            Map<String, String> coinsAndNamesMap = new HashMap<>();
             // uncomment this and run when new coins are added in southxchange
             //coinsAndNamesMap = getCoinsNameAndMap();

            JSONArray dataJsonArray = new JSONArray(jsonString);

            for (int i = 0; i < dataJsonArray.length(); i++) {

                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                String name = "";
                String label = dataJsonObject.getString("Currency");
                boolean isWalletActive = false;
                boolean isListingActive = false;

                if (coinsAndNamesMap.containsKey(label)) {
                    name = coinsAndNamesMap.get(label);
                }

                if (dataJsonObject.getInt("Status") == 2) {
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

        CoinStatus coinInfo = new CoinStatus(Market.SOUTHXCHANGE, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }

    private HashMap<String, String> getCoinsNameAndMap() {
        String jsonString = "";
        HashMap<String, String> map = new HashMap<>();
        try {
            jsonString = IOUtils.toString(new FileInputStream(new
                    File("southxchange_coinnames.json")), "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String coinName = jsonObject.getString("Name");
                String codeName = jsonObject.getString("Code");
                map.put(codeName, coinName);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
