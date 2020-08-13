package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

public class LiquiCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.liqui.io/api/3/info")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject pairsObject = jsonObject.getJSONObject("pairs");

            Set<String> keys = pairsObject.keySet();
            Iterator<String> iterator = keys.iterator();
            HashMap<String, String> map = new HashMap<>();
            //uncomment this to update
            //HashMap<String, String> map = getCoinNamesMap();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.contains("_btc")) {
                    JSONObject coinJSONObject = pairsObject.getJSONObject(key);


                    String name = "";
                    String label = (key.split("_btc")[0]).toUpperCase();
                    boolean isWalletActive = true;
                    boolean isListingActive = true;

                    if (map.containsKey(label)) {
                        name = map.get(label);
                    }


                    if (coinJSONObject.getInt("hidden") == 1) {
                        isListingActive = false;
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

        CoinStatus coinInfo = new CoinStatus(Market.LIQUI, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }

    private HashMap<String, String> getCoinNamesMap() {
        HashMap<String, String> map = new HashMap<>();

        try {
            File input = new File("liqui_balance.html");
            Document doc = Jsoup.parse(input, "UTF-8", "https://liqui.io/");

            Elements coinNameElements = doc.select("[class='trade-bold balance-coin-name']");
            Elements coinSymbolElements = doc.select("[class='balances-total-balance wallet-before balance-row-font']");


            for (int i = 0; i < coinNameElements.size(); i++) {
                String coinName = coinNameElements.get(i).text();
                String coinSymbol = coinSymbolElements.get(i).text().split(" ")[1];
                map.put(coinSymbol, coinName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


}
